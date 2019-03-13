package com.dracoo.jobreport.feature.connection;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionFragment extends Fragment {

    @BindView(R.id.rg_conn_antena)
    RadioGroup rg_conn_antena;
    @BindView(R.id.rg_conn_pedestial)
    RadioGroup rg_conn_pedestial;

    @BindView(R.id.rg_conn_access)
    RadioGroup rg_conn_access;

    private MessageUtils messageUtils;
    private RadioButton rb_selectedAntena;
    private String selectedAntena = "";
    private RadioButton rb_selectedPedestial;
    private String selectedPedestial = "";
    private RadioButton rb_selectedAccess;
    private String selectedAccess = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        messageUtils = new MessageUtils(getActivity());
        radioAntenaListener();
        radioPedestialListener();
        radioAccessListener();
    }

    private void radioAntenaListener(){
        rg_conn_antena.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedAntena =  getView().findViewById(i);
                selectedAntena = rb_selectedAntena.getText().toString();
            }
        });
    }

    private void radioPedestialListener(){
        rg_conn_pedestial.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedPedestial =  getView().findViewById(i);
                selectedPedestial = rb_selectedPedestial.getText().toString();
            }
        });
    }

    private void radioAccessListener(){
        rg_conn_access.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_selectedAccess =  getView().findViewById(i);
                selectedAccess = rb_selectedAccess.getText().toString();
            }
        });
    }




}
