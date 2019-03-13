package com.dracoo.jobreport.feature.xpoll;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dracoo.jobreport.R;

import butterknife.ButterKnife;

public class XpollActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xpoll);
        ButterKnife.bind(this);
    }
}
