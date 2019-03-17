package com.dracoo.jobreport.feature.useractivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dracoo.jobreport.R;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.MessageUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.rg_userAct_progress)
    RadioGroup rg_userAct_progress;
    @BindView(R.id.txt_userAct_name)
    EditText txt_userAct_name;
    @BindView(R.id.txt_userAct_service)
    EditText txt_userAct_service;
    @BindView(R.id.txt_userAct_phone)
    EditText txt_userAct_phone;
    @BindView(R.id.txt_userAct_name_pic)
    EditText txt_userAct_name_pic;
    @BindView(R.id.txt_userAct_jabatan)
    EditText txt_userAct_jabatan;
    @BindView(R.id.txt_userAct_picPhone)
    EditText txt_userAct_picPhone;
    @BindView(R.id.txt_userAct_locationName)
    EditText txt_userAct_locationName;
    @BindView(R.id.txt_userAct_customer)
    EditText txt_userAct_customer;
    @BindView(R.id.txt_userAct_remoteAddress)
    EditText txt_userAct_remoteAddress;
    @BindView(R.id.txt_userAct_city)
    EditText txt_userAct_city;
    @BindView(R.id.txt_userAct_kabupaten)
    EditText txt_userAct_kabupaten;
    @BindView(R.id.txt_userAct_proviency)
    EditText txt_userAct_proviency;
    @BindView(R.id.txt_userAct_lat)
    EditText txt_userAct_lat;
    @BindView(R.id.txt_userAct_long)
    EditText txt_userAct_long;

    private RadioButton rb_progress;
    private String selectedProgress;
    private MessageUtils messageUtils;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        setupGoogleAPI();
    }

    @Override
    public void onStart() {
        super.onStart();
        messageUtils = new MessageUtils(UserActivity.this);
        rbProgressListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGps();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void checkGps() {
        locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnabled && !isNetworkEnabled) {
            new AlertDialog.Builder(UserActivity.this)
                    .setTitle("GPS Off")
                    .setMessage("Mohon aktifka GPS terlebih dahulu")
                    .setIcon(R.drawable.ic_check)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        } else {
            mGoogleApiClient.connect();
        }
    }

    private void setupGoogleAPI() {
        // initialize Google API Client
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    private void rbProgressListener() {
        rg_userAct_progress.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                rb_progress = findViewById(i);
                selectedProgress = rb_progress.getText().toString();
            }
        });
    }

    @OnClick(R.id.imgB_userAct_submit)
    void submitUser() {
        messageUtils.toastMessage("coba", ConfigApps.T_DEFAULT);
    }

    @OnClick(R.id.imgB_userAct_cancel)
    void cancelUser() {
        messageUtils.toastMessage("coba menu", ConfigApps.T_DEFAULT);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationRequest request = new LocationRequest();
        request.setInterval(7 * 1000);
        request.setFastestInterval(3 * 1000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(
                this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocation.requestLocationUpdates(request, callback, Looper.myLooper());
    }

    @Override
    public void onConnectionSuspended(int i) { }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) { }

    private LocationCallback callback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
                txt_userAct_lat.setText(String.valueOf(mLastLocation.getLatitude()));
                txt_userAct_long.setText(String.valueOf(mLastLocation.getLongitude()));
            }
        }
    };
}
