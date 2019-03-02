package com.dracoo.jobreport.preparation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dracoo.jobreport.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.prg_splash)
    ProgressBar prgSplash;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        handler = new Handler();
    }


    @Override
    protected void onStart() {
        super.onStart();
        createDirectory();
    }


    private void createDirectory(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                prgSplash.setVisibility(View.VISIBLE);
                File mFile = new File(android.os.Environment.getExternalStorageDirectory().getPath() + "JobReport/images/");
                try {
                    if (!mFile.exists()) {
                        if (!mFile.mkdirs()) {
                            Log.d("####","Gagal create directory");
                        }
                    }

                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                prgSplash.setVisibility(View.GONE);
            }
        }, 1000);

    }
}
