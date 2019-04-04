package com.dracoo.jobreport.feature.documentation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.ImageAdapter;
import com.dracoo.jobreport.database.adapter.ImageConnTypeAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterImage;
import com.dracoo.jobreport.database.master.MasterImageConnType;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.dashboard.adapter.CustomList_Dashboard_Adapter;
import com.dracoo.jobreport.feature.documentation.adapter.CustomList_Doc_Adapter;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DocumentationFragment extends Fragment {
    private MessageUtils messageUtils;
    private Preference preference;
    private String[] arr_imgTitle;
    private String selectedImgTitle;
    private int selectedImagePosition = 0;
    private String filePath;
    private File imageToSave;
    private Dao<MasterImage, Integer> imageDao;
    private Dao<MasterTransHistory, Integer> transHistoryAdapter;
    private ArrayList<MasterImage> al_image;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

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
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
    }

    private void loadSpinner(String connType){
        ArrayList<MasterImageConnType> al_imgConn = new ImageConnTypeAdapter(getActivity()).load_imgConn(connType);
        arr_imgTitle = new String[al_imgConn.size()];

        int i = 0;
        for (MasterImageConnType mImgConn : al_imgConn){
            arr_imgTitle[i] = mImgConn.getImage_title().trim();
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
                selectedImagePosition = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void getImageCamera(){
        File imagesFolder =new File(android.os.Environment.getExternalStorageDirectory().getPath() + "/JobReport/images/"+selectedImgTitle);
        if (!imagesFolder.exists()) {
            if (imagesFolder.mkdirs()) {
                Log.d("Pembuatan Direktori Suskes ", "ok");
            }
        }

        filePath = "/JobReport/images/"
                + selectedImgTitle + "/"
                + selectedImgTitle +"_"+preference.getCustID()+ ".jpg";
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

//    protected void onResume(){
//        super.onResume();
//    }
    private void saveDataImage(){
        ArrayList<MasterImage> al_valImage = new ImageAdapter(getActivity())
                .val_dataImage(preference.getCustID(), preference.getUn(),
                        preference.getConnType(), selectedImagePosition);
        ArrayList<MasterImage> al_countImage = new ImageAdapter(getActivity())
                .load_dataImage(preference.getCustID(), preference.getUn());
        if (al_valImage.size() > 0){
            if (imageToSave.exists()){
                messageUtils.toastMessage("Image sudah ada, transaksi dibatalkan", ConfigApps.T_WARNING);
            }
        }else if (al_countImage.size() > 5){
            messageUtils.toastMessage("Jumlah Foto sudah 5, transaksi dibatalkan", ConfigApps.T_WARNING);
        } else{
            try{
                MasterImage mImage = new MasterImage();
                mImage.setId_site(preference.getCustID());
                mImage.setConn_type(preference.getConnType().trim());
                mImage.setImage_name(selectedImgTitle +"_"+preference.getCustID()+ ".jpg");
                mImage.setImage_position(selectedImagePosition);
                mImage.setImage_url(filePath.trim());
                mImage.setInsert_date(DateTimeUtils.getCurrentTime());
                mImage.setProgress_type(preference.getProgress().trim());
                mImage.setUn_user(preference.getUn().trim());

                imageDao.create(mImage);
                loadRcTrans();
                if (al_countImage.size() == 5){
                    transHistImage();
                }
            }catch (Exception e){
                messageUtils.toastMessage("Err insert image " +e.toString(), ConfigApps.T_ERROR);
            }
        }
    }

    private void transHistImage(){
        ArrayList<MasterTransHistory> al_valTransHist = new TransHistoryAdapter(getActivity())
                .val_trans(preference.getCustID(), preference.getUn(), getActivity().getString(R.string.machine_trans));
        if (al_valTransHist.size() > 0){
            try{
                MasterTransHistory mHist = transHistoryAdapter.queryForId(al_valTransHist.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.machine_trans));
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistoryAdapter.update(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success) + " diupdate", ConfigApps.T_SUCCESS);
            }catch (Exception e){
                messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR);
            }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(preference.getCustID());
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(getActivity().getString(R.string.machine_trans));
                mHist.setIs_submited(0);

                transHistoryAdapter.create(mHist);
                messageUtils.toastMessage(getActivity().getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
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
        }
        return list;
    }

    private void loadRcTrans(){
        if (preference.getCustID() != 0){
            rv_doc.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(getActivity());
            rv_doc.setLayoutManager(layoutManager);
            List<MasterImage> list = getList_Image();
            adapter = new CustomList_Doc_Adapter(getActivity(), list);
            rv_doc.setAdapter(adapter);
        }else{
            Log.d("###","ke sama dengan 0");
        }
    }



}


