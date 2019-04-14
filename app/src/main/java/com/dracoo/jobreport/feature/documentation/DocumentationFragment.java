package com.dracoo.jobreport.feature.documentation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ImageAdapter;
import com.dracoo.jobreport.database.adapter.ImageConnTypeAdapter;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterImage;
import com.dracoo.jobreport.database.master.MasterImageConnType;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.documentation.adapter.CustomList_Doc_Adapter;
import com.dracoo.jobreport.feature.documentation.contract.ItemCallback;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DocumentationFragment extends Fragment implements ItemCallback {
    private MessageUtils messageUtils;
    private Preference preference;
    private String[] arr_imgTitle;
    private String[] arr_imgUrl;
    private String[] arr_imgName;
    private String[] arr_imgFolder;
    private String selectedImgTitle;
    private String selectedImgFolder;
    private String customerName = "";
    private int selectedImagePosition = 0;
    private String filePath;
    private File imageToSave;
    private Dao<MasterImage, Integer> imageDao;
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private ArrayList<MasterImage> al_image;
    RecyclerView.LayoutManager layoutManager;
    private CustomList_Doc_Adapter adapter;
    private Handler handler;

    @BindView(R.id. imgV_doc_1)
    ImageView imgV_doc_1;
    @BindView(R.id.imgB_doc_confirm)
    ImageButton imgB_doc_confirm;
    @BindView(R.id.rv_doc)
    RecyclerView rv_doc;
    @BindView(R.id.lbl_doc_empty)
    TextView lbl_doc_empty;
    @BindView(R.id.spinner_doc)
    Spinner spinner_doc;

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

        if (preference.getConnType().equals("")){
            messageUtils.snackBar_message("Mohon diinput Menu Connection terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
            imgB_doc_confirm.setEnabled(false);
            imgV_doc_1.setEnabled(false);
        }else{
            imgB_doc_confirm.setEnabled(true);
            imgV_doc_1.setEnabled(true);
            loadSpinner(preference.getConnType().trim());
        }

        try {
            imageDao = new ImageAdapter(getActivity()).getAdapter();
            transHistoryAdapter = new TransHistoryAdapter(getActivity()).getAdapter();
        }catch (Exception e){}
    }


    @OnClick(R.id.imgV_doc_1)
    void getImage(){
        getImageCamera();
    }

    @OnClick(R.id.imgB_doc_confirm)
    void getUpload(){
        if (preference.getCustID() == 0){
            messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(),ConfigApps.SNACKBAR_NO_BUTTON);
        }else if (!new ImageAdapter(getActivity()).isImageEmpty(preference.getUn(), preference.getCustID())){
            messageUtils.snackBar_message("Mohon diambil gambar terlebih dahulu", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
        } else{
            convertPdf();
        }
    }

    private void convertPdf(){
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ArrayList<MasterImage> al_countImage = new ImageAdapter(getActivity())
                        .load_dataImage(preference.getCustID(), preference.getUn());
                if (al_countImage.size() == 5){
                    ArrayList<MasterInfoSite> alInfo = new InfoSiteAdapter(getActivity()).load_site(preference.getCustID(), preference.getUn());
                    if (alInfo.size() > 0){
                        File mFilePdf = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/ImagePdf/"+alInfo.get(0).getCustomer_name());
                        customerName = alInfo.get(0).getCustomer_name().trim();
                        if (!mFilePdf.exists()) {
                            if (!mFilePdf.mkdirs()) {
                                Log.d("####","Gagal create directory");
                            }
                        }
                        File mFileValidationPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/ImagePdf/"+alInfo.get(0).getCustomer_name() + "/"+alInfo.get(0).getCustomer_name()+".pdf");
                        if (mFileValidationPdf.exists()){
                            mFileValidationPdf.delete();
                        }

                        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
                        try{
                            PdfWriter.getInstance(document, new FileOutputStream(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/ReportPdf/ImagePdf/"+alInfo.get(0).getCustomer_name() + "/"+alInfo.get(0).getCustomer_name()+".pdf"));
                            document.open();
                            if (al_image.size() > 0){
                                arr_imgName = new String[al_image.size()];
                                arr_imgUrl = new String[al_image.size()];
                                float mHeadingFontSize = 20.0f;
                                BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
                                BaseFont urName = BaseFont.createFont("assets/Asap-Regular.ttf", "UTF-8", BaseFont.EMBEDDED);
                                Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);

                                int i = 0;
                                for (MasterImage mImage : al_image){
                                    document.newPage();
                                    arr_imgName[i] = mImage.getImage_name().trim();
                                    arr_imgUrl[i] = mImage.getImage_url().trim();
                                    String stImageUrl = android.os.Environment.getExternalStorageDirectory().toString()+""+arr_imgUrl[i];

                                    Image image = Image.getInstance(stImageUrl);
                                    float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                                            - document.rightMargin() - 0) / image.getWidth()) * 100; // 0 means you have no indentation. If you have any, change it.
                                    image.scalePercent(scaler);
                                    image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

                                    //add title
                                    Chunk mOrderIdChunk = new Chunk("Title Image: " + arr_imgName[i], mOrderIdFont);
                                    Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
                                    document.add(mOrderIdParagraph);
                                    document.add(image);

                                    i++;
                                }
                                document.close();
                                messageUtils.toastMessage("Dokumen sukses diconvert ke pdf", ConfigApps.T_SUCCESS);
                                File mFileResultPdf = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "/JobReport/ReportPdf/ImagePdf/"+alInfo.get(0).getCustomer_name() + "/"+alInfo.get(0).getCustomer_name()+".pdf");
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
                                    }
                                } catch(Exception e) {
                                    messageUtils.toastMessage("err share message " +e.toString(), ConfigApps.T_ERROR);
                                }
                            }
                        }catch (Exception e){
                            messageUtils.toastMessage("err convert image" +e.toString(), ConfigApps.T_ERROR);
                        }
                    }else{
                        messageUtils.snackBar_message(getActivity().getString(R.string.customer_validation), getActivity(),ConfigApps.SNACKBAR_NO_BUTTON);
                    }
                }else{
                    messageUtils.snackBar_message("Mohon dilengkapi foto yang belum diinput", getActivity(), ConfigApps.SNACKBAR_NO_BUTTON);
                }
            }
        }, 1000);
    }

    private void loadSpinner(String connType){
        ArrayList<MasterImageConnType> al_imgConn = new ImageConnTypeAdapter(getActivity()).load_imgConn(connType);
        arr_imgTitle = new String[al_imgConn.size()];
        arr_imgFolder = new String[al_imgConn.size()];

        int i = 0;
        for (MasterImageConnType mImgConn : al_imgConn){
            arr_imgTitle[i] = mImgConn.getImage_title().trim();
            arr_imgFolder[i] = mImgConn.getImage_folder();
            i++;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, arr_imgTitle);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_doc.setAdapter(adapter);
        spinner_doc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int index = spinner_doc.getSelectedItemPosition();
                selectedImgTitle = arr_imgTitle[index];
                selectedImgFolder = arr_imgFolder[index];
                selectedImagePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void getImageCamera(){
        File imagesFolder =new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/images/"+selectedImgFolder);
        if (!imagesFolder.exists()) {
            if (imagesFolder.mkdirs()) {
                Log.d("Direktori Sukses ", "ok");
            }
        }

        filePath = "/JobReport/images/"
                + selectedImgFolder + "/"
                + selectedImgFolder +"_"+preference.getCustID()+ ".jpg";
        imageToSave = new File(android.os.Environment.getExternalStorageDirectory().getPath(), filePath);

        Intent capIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File image = new File(imageToSave.getAbsolutePath());
        Uri uriSavedImage = Uri.fromFile(image);
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
                        preference.getConnType(), selectedImagePosition);
        ArrayList<MasterImage> al_countImage = new ImageAdapter(getActivity())
                .load_dataImage(preference.getCustID(), preference.getUn());
        if (al_valImage.size() > 0){
            if (imageToSave.exists()){
                messageUtils.toastMessage("Image sudah ada, transaksi dibatalkan", ConfigApps.T_WARNING);
                imageToSave.delete();
            }
        }else if (al_countImage.size() > 5){
            messageUtils.toastMessage("Jumlah Foto sudah 5, transaksi dibatalkan", ConfigApps.T_WARNING);
        } else{
            try{
                MasterImage mImage = new MasterImage();
                mImage.setId_site(preference.getCustID());
                mImage.setConn_type(preference.getConnType().trim());
                mImage.setImage_name(selectedImgTitle+".jpg");
                mImage.setImage_position(selectedImagePosition);
                mImage.setImage_url(filePath.trim());
                mImage.setInsert_date(DateTimeUtils.getCurrentTime());
                mImage.setProgress_type(preference.getProgress().trim());
                mImage.setUn_user(preference.getUn().trim());

                imageDao.create(mImage);
                filePath = "";
                imageToSave = null;
                selectedImgFolder = "";
                selectedImgTitle = "";
                transHistImage();
                loadRcImage();

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
}


