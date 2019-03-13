package com.dracoo.jobreport.feature.datam2m;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dracoo.jobreport.R;

import butterknife.ButterKnife;

public class DataM2mActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_m2m);
        ButterKnife.bind(this);
    }
}
