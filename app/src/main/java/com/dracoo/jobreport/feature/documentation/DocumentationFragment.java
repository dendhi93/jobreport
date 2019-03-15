package com.dracoo.jobreport.feature.documentation;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class DocumentationFragment extends Fragment {
    private MessageUtils messageUtils;

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
    }


    @OnClick(R.id.imgV_doc_1)
    void getImage(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_doc_confirm)
    void getUpload(){
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
    }
}


