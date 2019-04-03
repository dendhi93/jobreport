package com.dracoo.jobreport.feature.documentation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import com.dracoo.jobreport.database.adapter.ImageConnTypeAdapter;
import com.dracoo.jobreport.database.master.MasterImageConnType;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DocumentationFragment extends Fragment {
    private MessageUtils messageUtils;
    private Preference preference;
    private String[] arr_imgTitle;
    private String selectedImgTitle;

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

    }


    @OnClick(R.id.imgV_doc_1)
    void getImage(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }
}


