package com.dracoo.jobreport.feature.vsatparameter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParameterActivity extends AppCompatActivity {

    @BindView(R.id.rg_par_subnetmask)
    RadioGroup rg_par_subnetmask;

    private String selectedParameter = "";
    private RadioButton rbParameter;
    private MessageUtils messageUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(ParameterActivity.this);
        displayParamRadio();

    }

    private void displayParamRadio(){
        rg_par_subnetmask.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rbParameter =  findViewById(i);
                selectedParameter = rbParameter.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_par_submit)
    void submitPar(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_par_menu)
    void chooseMenu(){
        messageUtils.toastMessage("coba menu", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_par_cancel)
    void cancelPar(){
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }
}
