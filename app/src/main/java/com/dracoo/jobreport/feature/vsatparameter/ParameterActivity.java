package com.dracoo.jobreport.feature.vsatparameter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dracoo.jobreport.R;

import butterknife.ButterKnife;

public class ParameterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parameter);
        ButterKnife.bind(this);
    }
}
