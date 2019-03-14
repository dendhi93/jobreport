package com.dracoo.jobreport.feature.replace;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReplaceActivity extends AppCompatActivity {
    private MessageUtils messageUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace);
        ButterKnife.bind(this);
    }

    @Override
    public void onStart(){
        super.onStart();
        messageUtils = new MessageUtils(ReplaceActivity.this);
    }

    @OnClick(R.id.imgB_rep_submit)
    void submitRep(){
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_rep_menu)
    void chooseRepMenu(){
        messageUtils.toastMessage("coba menu", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_rep_cancel)
    void cancelRep(){
        messageUtils.toastMessage("coba2", ConfigApps.T_DEFAULT);
    }
}
