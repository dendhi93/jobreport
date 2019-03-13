package com.dracoo.jobreport.feature.vsatparameter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

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
                rbParameter =  (RadioButton) findViewById(i);
                selectedParameter = rbParameter.getText().toString();
            }
        });
    }
}
