package com.dracoo.jobreport.feature.documentation;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ImageAdapter;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterImage;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.documentation.adapter.CustomList_Doc_Adapter;
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;
import com.dracoo.jobreport.feature.useractivity.UserActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DocumentationFragment extends Fragment implements ItemCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private MessageUtils messageUtils;
    private Preference preference;
    private JobReportUtils jobUtils;
    private String[] arr_imgUrl;
    private String[] arr_imgName;
    private String selectedImgTitle;
    private String selectedImgFolder;
    private String customerName = "";
    private String filePath;
    private File imageToSave;
    private Dao<MasterImage, Integer> imageDao;
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private ArrayList<MasterImage> al_image;
    RecyclerView.LayoutManager layoutManager;
    private CustomList_Doc_Adapter adapter;
    private Handler handler;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocation;

    @BindView(R.id. imgV_doc_1)
    ImageView imgV_doc_1;
    @BindView(R.id.imgB_doc_confirm)
    ImageButton imgB_doc_confirm;
    @BindView(R.id.rv_doc)
    RecyclerView rv_doc;
    @BindView(R.id.lbl_doc_empty)
    TextView lbl_doc_empty;
    @BindView(R.id.txt_doc_docType)
    EditText txt_doc_docType;
    @BindView(R.id.txt_doc_docDescription)
    EditText txt_doc_docDescription;
    @BindView(R.id.prd_doc)
    ProgressBar prd_doc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_documentation, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        messageUtils = new MessageUtils(getActivity());
        preference = new Preference(getActivity());
        jobUtils = new JobReportUtils();
        prd_doc.setVisibility(View.GONE);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());
        setupMAPAPI();
        if (preference.getConnType().equals("")){
            messageUtils.snackBar_message("Mohon diinput Menu Connection terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            imgB_doc_confirm.setEnabled(false);
            imgV_doc_1.setEnabled(false);
        }else{
            imgB_doc_confirm.setEnabled(true);
            imgV_doc_1.setEnabled(true);
        }

        try {
            imageDao = new ImageAdapter(getActivity()).getAdapter();
            transHistoryAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }

    private void setupMAPAPI() {
        // initialize Google API Client
        mGoogleApiClient = new GoogleApiClient
                .Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void checkGps() {
        locationManager = (LocationManager)  getActivity().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("GPS Off")
                    .setMessage("Mohon aktifkan GPS terlebih dahulu")
                    .setIcon(R.drawable.ic_check)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        } else {
            mGoogleApiClient.connect();
        }
    }

    @OnClick(R.id.imgV_doc_1)
    void getImage(){
        if (txt_doc_docType.getText().toString().trim().equals("") || txt_doc_docDescription.getText().toString().trim().equals("")){
            messageUtils.snackBar_message(getActivity().getString(R.string.emptyString), getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        }else{
            selectedImgFolder = txt_doc_docType.getText().toString().trim();
            selectedImgTitle = txt_doc_docType.getText().toString().trim();
            getImageCamera();
        }
    }

    @OnClick(R.id.imgB_doc_confirm)
    void getUpload(){
        if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(),ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (!new ImageAdapter(getActivity()).isImageEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.snackBar_message("Mohon diambil gambar terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else{
            prd_doc.setVisibility(View.VISIBLE);
            convertPdf();
        }
    }

    private void convertPdf(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (al_image.size() == 6){
                    ArrayList<MasterInfoSite> alInfo = new InfoSiteAdapter(getActivity()).load_site(preference.getCustID(), preference.getUn());
                    if (alInfo.size() > 0){
                        File mFilePdf = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/ImagePdf/"+preference.getCustName());
                        customerName = alInfo.get(0).getCustomer_name().trim();
                        if (!mFilePdf.exists()) {
                            if (!mFilePdf.mkdirs()) {
                                Log.d("####","Gagal create directory");
                            }
                        }
                        File mFileValidationPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/ImagePdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf");
                        if (mFileValidationPdf.exists()){
                            mFileValidationPdf.delete();
                        }

                        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
                        try{
                            PdfWriter.getInstance(document, new FileOutputStream(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/ImagePdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf"));
                            document.open();
                            if (al_image.size() > 0){
                                arr_imgName = new String[al_image.size()];
                                arr_imgUrl = new String[al_image.size()];
                                float mHeadingFontSize = 20.0f;
                                float mcontentFontSize = 10.0f;
                                BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
                                BaseFont urName = BaseFont.createFont("assets/Asap-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                                Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
                                Font contentFont = new Font(urName, mcontentFontSize, Font.NORMAL, BaseColor.BLACK);

                                PdfPTable tableTitle = new PdfPTable(1);
                                tableTitle.setHorizontalAlignment(Element.ALIGN_LEFT);
                                tableTitle.addCell(JobReportUtils.bottomLineCell("Documentation", mOrderIdFont));
                                document.add(tableTitle);
                                document.add(jobUtils.singleSpace(contentFont));
                                PdfPTable tableTitle2 = new PdfPTable(2);
                                tableTitle2.setHorizontalAlignment(Element.ALIGN_LEFT);
                                tableTitle2.addCell(JobReportUtils.borderlessCell("Documentation : ______________________", contentFont));
                                tableTitle2.addCell(JobReportUtils.borderlessCell("Date : _______ / _______ / ___________", contentFont));
                                document.add(tableTitle2);
                                document.add(jobUtils.singleSpace(contentFont));

                                int i = 0;
                                PdfPTable contentTable = new PdfPTable(2);
                                contentTable.setHorizontalAlignment(Element.ALIGN_LEFT);
                                for (MasterImage mImage : al_image){
                                    arr_imgName[i] = mImage.getImage_description().trim();
                                    arr_imgUrl[i] = mImage.getImage_url().trim();
                                    String stImageUrl = android.os.Environment.getExternalStorageDirectory().toString()+""+arr_imgUrl[i];
                                    Image image = Image.getInstance(stImageUrl);
                                    image.setAbsolutePosition(100, 150);

                                    PdfPCell cell = new PdfPCell();
                                    cell.addElement(Image.getInstance(stImageUrl));
                                    Paragraph p = new Paragraph(arr_imgName[i], contentFont);
                                    p.setAlignment(Element.ALIGN_CENTER);
                                    cell.addElement(p);
                                    contentTable.addCell(cell);
                                    i++;
                                }
                                document.add(contentTable);
                                document.close();
                                prd_doc.setVisibility(View.GONE);

                                messageUtils.toastMessage("Dokumen sukses diconvert ke pdf", ConfigApps.T_SUCCESS);
                                File mFileResultPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/ImagePdf/"+preference.getCustName() + "/"+preference.getCustName()+".pdf");
                                String subjectEmail = "Kepada yth,\nBpk/Ibu Admin\n\nBerikut saya lampirkan Report Customer " +customerName+
                                        "\n\nDemikian yang bisa saya sampaikan\nTerima Kasih\n\n\n " + preference.getUn().
                                        trim().toLowerCase(java.util.Locale.getDefault());
                                try {
                                    if (mFilePdf.exists()) {
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.setType("text/plain");
                                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Report Customer " +customerName);
                                        shareIntent.putExtra(Intent.EXTRA_TEXT, subjectEmail);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(mFileResultPdf));
                                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                                    }else{
                                        messageUtils.toastMessage("File Tidak ditemukan", ConfigApps.T_WARNING);
                                        prd_doc.setVisibility(View.GONE);
                                    }
                                } catch(Exception e) {
                                    messageUtils.toastMessage("err share message " +e.toString(), ConfigApps.T_ERROR);
                                    prd_doc.setVisibility(View.GONE);
                                }
                            }
                        }catch (Exception e){
                            messageUtils.toastMessage("err convert image" +e.toString(), ConfigApps.T_ERROR);
                            prd_doc.setVisibility(View.GONE);
                        }
                    }else{
                        messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(),ConfigApps.SNACKBAR_NO_BUTTON);
                        prd_doc.setVisibility(View.GONE);
                    }
                }else{
                    messageUtils.snackBar_message("Mohon dilengkapi foto yang belum diinput", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
                    prd_doc.setVisibility(View.GONE);
                }
            }
        }, 1000);
    }

    private void getImageCamera(){
        if (getActivity() != null){ JobReportUtils.hideKeyboard(getActivity()); }

        if (selectedImgFolder.contains(" ")){
            String[] split = selectedImgFolder.split(" ");
            selectedImgFolder = split[0]+""+split[1];
        }
        File imagesFolder =new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/images/"+preference.getCustName()+"/"+selectedImgFolder);
        if (!imagesFolder.exists()) {
            if (imagesFolder.mkdirs()) {
                Log.d("Direktori Sukses ", "ok");
            }
        }

        filePath = "/JobReport/images/"+preference.getCustName()+"/"
                + selectedImgFolder + "/"
                + selectedImgFolder +"_"+preference.getCustID()+ ".jpg";
        imageToSave = new File(android.os.Environment.getExternalStorageDirectory().getPath(), filePath);

        Intent capIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image = new File(imageToSave.getAbsolutePath());
        Uri uriSavedImage;
        if (Build.VERSION.SDK_INT >= 24) {
            uriSavedImage = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", image);
            capIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else { uriSavedImage = Uri.fromFile(image); }
        capIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        capIntent.putExtra("return-data", true);
        startActivityForResult(capIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }

        if (requestCode == 1){
            try {
                if (filePath != null) {
                    File imageFile = new File(imageToSave.getAbsolutePath());
                    Bitmap photo = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    Bitmap newBitmap = Bitmap.createScaledBitmap(photo, 350, 350, true);
//                    imgV_doc_1.setImageBitmap(newBitmap);
                    FileOutputStream out = new FileOutputStream(imageFile);
                    newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    saveDataImage();
                }
            } catch (Exception e) {
                messageUtils.toastMessage("failed to display image " +e.toString(), ConfigApps.T_ERROR);
                Log.d("###","" +e.toString());
            }
        }
    }

    private void saveDataImage(){
        ArrayList<MasterImage> al_valImage = new ImageAdapter(getActivity())
                .val_dataImage(preference.getCustID(), preference.getUn(),
                        preference.getConnType(), selectedImgTitle);
        if (al_image.size() > 6){
            messageUtils.toastMessage("Jumlah Foto sudah 6, transaksi dibatalkan", ConfigApps.T_WARNING);
        }else if (al_valImage.size() > 0){
            if (imageToSave.exists()){
                messageUtils.toastMessage("Image sudah ada, transaksi dibatalkan", ConfigApps.T_WARNING);
                imageToSave.delete();
            }
        }else{
            try{
                MasterImage mImage = new MasterImage();
                mImage.setId_site(preference.getCustID());
                mImage.setConn_type(preference.getConnType().trim());
                mImage.setImage_name(selectedImgTitle+".jpg");
                mImage.setImage_url(filePath.trim());
                mImage.setInsert_date(DateTimeUtils.getCurrentTime());
                mImage.setProgress_type(preference.getProgress().trim());
                mImage.setUn_user(preference.getUn().trim());
                mImage.setImage_description(txt_doc_docDescription.getText().toString().trim());

                imageDao.create(mImage);
                filePath = "";
                imageToSave = null;
                selectedImgFolder = "";
                selectedImgTitle = "";
                transHistImage();
                loadRcImage();
                txt_doc_docType.setText("");
                txt_doc_docDescription.setText("");
                txt_doc_docDescription.clearFocus();

            }catch (Exception e){
                messageUtils.toastMessage("Err insert image " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    private void transHistImage(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.doc_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistoryAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.doc_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistoryAdapter.update(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.doc_trans));
                mHist.setIs_submited(0);

                transHistoryAdapter.create(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                if (getActivity() != null){
                    JobReportUtils.hideKeyboard(getActivity());
                }
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }


    public List<MasterImage> getList_Image(){
        List<MasterImage> list = new ArrayList<>();
        try {
            al_image = new ImageAdapter(getActivity()).load_dataImage(preference.getCustID(), preference.getUn());
            if (al_image.size() > 0){
                list = al_image;
                rv_doc.setVisibility(View.VISIBLE);
                lbl_doc_empty.setVisibility(View.GONE);
            }else{
                rv_doc.setVisibility(View.GONE);
                lbl_doc_empty.setVisibility(View.VISIBLE);
            }
        }catch (Exception e){
            rv_doc.setVisibility(View.GONE);
            lbl_doc_empty.setVisibility(View.VISIBLE);
            Log.d("###", "ke catch list " +e.toString());
            messageUtils.toastMessage("err list " +e.toString(), ConfigApps.T_DEFAULT);
        }
        return list;
    }

    private void loadRcImage(){
        if (preference.getCustID() != 0 || !preference.getConnType().equals("")){
            rv_doc.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            rv_doc.setLayoutManager(layoutManager);
            List<MasterImage> list = getList_Image();
            adapter = new CustomList_Doc_Adapter(getActivity(), list);
            adapter.theCallBack(this);
            rv_doc.setAdapter(adapter);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        loadRcImage();
        checkGps();
        prd_doc.setVisibility(View.GONE);
    }

    @Override
    public void itemSelected(final int pos, final String imageUrl) {
        if (getActivity() != null){
            new AlertDialog.Builder(getActivity())
                    .setTitle("Warning")
                    .setMessage("Apakah anda yakin ingin hapus gambar ?")
                    .setIcon(R.drawable.ic_exclamation_32)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteImage(pos, imageUrl);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    public void selectedImage(String imageUrl) {
        AlertDialog.Builder alertadd = new AlertDialog.Builder(getActivity());
        final ImageView imgV = new ImageView(getActivity());

        File file = new File(android.os.Environment.getExternalStorageDirectory().getPath(),imageUrl);
        Uri imageUri = Uri.fromFile(file);
        Glide.with(getActivity())
                .load(imageUri)
                .into(imgV);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(2, 2, 2, 2);
        LinearLayout linearImg = new LinearLayout(getActivity());
        linearImg.setOrientation(LinearLayout.VERTICAL);
        linearImg.addView(imgV,layoutParams);
        alertadd.setView(linearImg);
        alertadd.setNegativeButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alertadd.create();
        alert.show();
    }

    private void deleteImage(int pos, String imageUrl){
        try{
            File file = new File(android.os.Environment.getExternalStorageDirectory().getPath(),imageUrl);
            if (file.exists()){
                file.delete();
                imageDao.deleteById(pos);
                messageUtils.toastMessage("Image sukses dihapus", ConfigApps.T_SUCCESS);
                loadRcImage();
            }
        }catch (Exception e){ messageUtils.toastMessage("failed to delete image " +e.toString(), ConfigApps.T_ERROR); }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setInterval(7 * 1000);
        request.setFastestInterval(3 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(
                this.getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocation.requestLocationUpdates(request, callback, Looper.myLooper());
    }

    private LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            String splitConvertLat = "0.0";
            String splitConvertLong = "0.0";
            String[] splitDecimalCoor;
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
                JobReportUtils jobUtils = new JobReportUtils();
                String convertCoor = jobUtils.convertCoordinat(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                if (convertCoor.contains("W ")){
                    splitDecimalCoor = convertCoor.split("W ");
                    splitConvertLat = splitDecimalCoor[0];
                    splitConvertLong = "W "+splitDecimalCoor[1];
                }else if(convertCoor.contains("E ")){
                    splitDecimalCoor = convertCoor.split("E ");
                    splitConvertLat = splitDecimalCoor[0];
                    splitConvertLong = "E "+splitDecimalCoor[1];
                }

                messageUtils.toastMessage(" convertLat " +splitConvertLat.trim() +"\nconvertLong " +splitConvertLong, ConfigApps.T_INFO);
            }
        }
    };

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

}


