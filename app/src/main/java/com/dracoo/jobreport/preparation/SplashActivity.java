package com.dracoo.jobreport.preparation;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.DatabaseAdapter;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.Dialogs;
import com.dracoo.jobreport.util.MessageUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.prg_splash)
    ProgressBar prgSplash;
    private Handler handler;
    private DatabaseAdapter databaseAdapter;
    private MessageUtils messageUtils;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        messageUtils = new MessageUtils(SplashActivity.this);
    }


    @Override
    protected void onStart() {
        super.onStart();
        checkGps();
        try {
            databaseAdapter = new DatabaseAdapter(this);
            databaseAdapter.CreateDatabase();
        } catch (Exception e) {messageUtils.toastMessage("failed to create Database "+e, ConfigApps.T_ERROR);}
    }

    private void checkGps(){
        prgSplash.setVisibility(View.VISIBLE);
        handler = new Handler(Looper.getMainLooper());
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            prgSplash.setVisibility(View.INVISIBLE);
            Dialogs.showDialog(handler, SplashActivity.this, "Warning", "Gps OFf, mohon Akrifkan Gps", true);
        }else{
            createDirectory();
        }

    }

    private void createDirectory(){
        handler = new Handler();
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
                prgSplash.setVisibility(View.INVISIBLE);
            }
        }, 1000);

    }
}
