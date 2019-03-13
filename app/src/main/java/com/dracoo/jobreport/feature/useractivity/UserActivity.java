package com.dracoo.jobreport.feature.useractivity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dracoo.jobreport.R;

import butterknife.ButterKnife;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
    }
}
