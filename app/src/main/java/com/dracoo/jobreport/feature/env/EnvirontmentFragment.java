package com.dracoo.jobreport.feature.env;

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


public class EnvirontmentFragment extends Fragment {
    private MessageUtils messageUtils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_environtment, container, false);
        ButterKnife.bind(this, view);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
    }

    @OnClick(R.id.imgB_env_submit)
    void submitEnv(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_env_cancel)
    void cancelEnv(){
        messageUtils.toastMessage("coba 2", ConfigApps.T_DEFAULT);
    }



}
