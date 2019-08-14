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
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.InfoSiteAdapter;
import com.dracoo.jobreport.database.adapter.JobDescAdapter;
import com.dracoo.jobreport.database.adapter.TransHistoryAdapter;
import com.dracoo.jobreport.database.master.MasterInfoSite;
import com.dracoo.jobreport.database.master.MasterJobDesc;
import com.dracoo.jobreport.database.master.MasterTransHistory;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.Dialogs;
import com.dracoo.jobreport.util.JobReportUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.j256.ormlite.dao.Dao;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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
    @BindView(R.id.txt_userAct_remoteName)
    EditText txt_userAct_remoteName;
    @BindView(R.id.txt_userAct_ttwo)
    EditText txt_userAct_ttwo;
    @BindView(R.id.sp_user_connType)
    Spinner sp_user_connType;
    @BindView(R.id.imgB_userAct_cancel)
    ImageButton imgB_userAct_cancel;
    @BindView(R.id.imgB_userAct_submit)
    ImageButton imgB_userAct_submit;

    private MessageUtils messageUtils;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private LocationManager locationManager;
    private FusedLocationProviderClient mFusedLocation;
    private AwesomeValidation awesomeValidation;
    private Preference preference;
    private Dao<MasterJobDesc, Integer> jobDescAdapter;
    private Dao<MasterInfoSite, Integer> infoSiteAdapter;
    private Dao<MasterTransHistory, Integer> transHistAdapter;
    private String[] arrConnType;
    private String selectedConnType = "";
    private String intentEditUser;
    private ArrayList<MasterInfoSite> al_infoSite;
    private ArrayList<MasterJobDesc> al_jobDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        mFusedLocation = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        setupGoogleAPI();
    }

    @Override
    public void onStart() {
        super.onStart();
        messageUtils = new MessageUtils(UserActivity.this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        preference = new Preference(UserActivity.this);
        getUserValidation();
        displaySpinner();

        if (!preference.getUn().equals("")){
            txt_userAct_name.setText(preference.getUn().trim());
            txt_userAct_phone.setText(preference.getPhone().trim());
            txt_userAct_service.setText(preference.getServicePoint().trim());
        }else{ Dialogs.showDismissDialog(this, "Warning", "Username terhapus, mohon keluar aplikasi dan login kembali" ); }

        try{
            jobDescAdapter = new JobDescAdapter(getApplicationContext()).getAdapter();
            infoSiteAdapter = new InfoSiteAdapter(getApplicationContext()).getAdapter();
            transHistAdapter = new TransHistoryAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){ Log.d("User err Adapter ",""+e); }

        txt_userAct_lat.setText("0.0");
        txt_userAct_long.setText("0.0");
        editValidation();
    }

    private void editValidation(){
        try{
            intentEditUser = getIntent().getExtras().getString(ConfigApps.EXTRA_CALLER_VIEW);
            if (intentEditUser.equals("") || intentEditUser == null){
                imgB_userAct_cancel.setVisibility(View.VISIBLE);
                imgB_userAct_submit.setVisibility(View.VISIBLE);
            }else {
                al_infoSite = new InfoSiteAdapter(getApplicationContext()).load_site(preference.getCustID(), preference.getUn());
                al_jobDesc = new JobDescAdapter(getApplicationContext()).load_trans(preference.getCustID(), preference.getUn());
                if (al_infoSite.size() > 0 && al_jobDesc.size() > 0 ){
                    txt_userAct_ttwo.setText(al_infoSite.get(0).getTtwo().trim());
                    txt_userAct_name_pic.setText(al_jobDesc.get(0).getName_pic().trim());
                    txt_userAct_jabatan.setText(al_jobDesc.get(0).getJabatan_desc().trim());
                    txt_userAct_picPhone.setText(al_jobDesc.get(0).getPic_phone().trim());
                    txt_userAct_locationName.setText(al_infoSite.get(0).getLocation_name().trim());
                    txt_userAct_customer.setText(al_infoSite.get(0).getCustomer_name().trim());
                    txt_userAct_remoteAddress.setText(al_infoSite.get(0).getRemote_address().trim());
                    txt_userAct_remoteName.setText(al_infoSite.get(0).getRemote_name().trim());
                    txt_userAct_city.setText(al_infoSite.get(0).getCity().trim());
                    txt_userAct_kabupaten.setText(al_infoSite.get(0).getKabupaten().trim());
                    txt_userAct_proviency.setText(al_infoSite.get(0).getProv().trim());
                    String progresssType = preference.getProgress().trim();
                    if (progresssType.trim().equals("CM")){
                        sp_user_connType.setSelection(1);
                    }else if(progresssType.trim().equals("PM")){
                        sp_user_connType.setSelection(2);
                    }

                    imgB_userAct_cancel.setVisibility(View.GONE);
                    imgB_userAct_submit.setVisibility(View.GONE);
                }else{
                    imgB_userAct_cancel.setVisibility(View.VISIBLE);
                    imgB_userAct_submit.setVisibility(View.VISIBLE);
                }
            }
        }catch (Exception e){
            imgB_userAct_cancel.setVisibility(View.VISIBLE);
            imgB_userAct_submit.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        try{
            if (!intentEditUser.equals("") || intentEditUser != null){ getMenuInflater().inflate(R.menu.menu, menu); }
        }catch (Exception e){}
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkGps();
    }

    private void displaySpinner(){
        arrConnType  = new String[]{"Jenis Progress","CM", "PM"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrConnType);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        sp_user_connType.setAdapter(adapter);
        sp_user_connType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0){ selectedConnType  = adapter.getItem(position); }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
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


    @OnClick(R.id.imgB_userAct_submit)
    void submitUser() {
        try{
            if (txt_userAct_lat.getText().toString().trim().equals("")
                    || txt_userAct_long.getText().toString().trim().equals("")){
                messageUtils.snackBar_message("Koordinat belum didapatkan, mohon dipastikan gps hidup dan terkoneksi jaringan",
                        UserActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            } else if (selectedConnType.trim().equals("")){}
            else if (txt_userAct_name_pic.getText().toString().trim().equals("") ||
                    txt_userAct_jabatan.getText().toString().trim().equals("") ||
                    txt_userAct_picPhone.getText().toString().trim().equals("") ||
                    txt_userAct_locationName.getText().toString().trim().equals("") ||
                    txt_userAct_customer.getText().toString().trim().equals("") ||
                    txt_userAct_remoteAddress.getText().toString().trim().equals("") ||
                    txt_userAct_city.getText().toString().trim().equals("") ||
                    txt_userAct_kabupaten.getText().toString().trim().equals("") ||
                    txt_userAct_proviency.getText().toString().trim().equals("") ||
                    txt_userAct_remoteName.getText().toString().trim().equals("") ||
                    txt_userAct_ttwo.getText().toString().trim().equals("")){
                messageUtils.snackBar_message(getString(R.string.emptyString), UserActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            } else if (txt_userAct_picPhone.getText().length() < 10){
                messageUtils.snackBar_message("No handphone pic kurang dari 10 angka",
                        UserActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
            } else{ infoSiteTrans(); }
        }catch (Exception e){
            messageUtils.snackBar_message("Mohon dipilih tipe progress", UserActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        }

    }

    @OnClick(R.id.imgB_userAct_cancel)
    void cancelUser() {
        emptyUserText();
    }

    private void infoSiteTrans(){
        try{
            MasterInfoSite mInfoSite = infoSiteAdapter.queryForId(preference.getCustID());
            mInfoSite.setLocation_name(txt_userAct_locationName.getText().toString().trim());
            mInfoSite.setUpdate_date(DateTimeUtils.getCurrentTime());
            mInfoSite.setRemote_address(txt_userAct_remoteAddress.getText().toString().trim());
            mInfoSite.setCity(txt_userAct_city.getText().toString().trim());
            mInfoSite.setKabupaten(txt_userAct_kabupaten.getText().toString().trim());
            mInfoSite.setProv(txt_userAct_proviency.getText().toString().trim());
            mInfoSite.setLat(txt_userAct_lat.getText().toString().trim());
            mInfoSite.setLongitude(txt_userAct_long.getText().toString().trim());
            mInfoSite.setProgress_type(selectedConnType.trim());
            mInfoSite.setTtwo(txt_userAct_ttwo.getText().toString().trim());
            mInfoSite.setUpdate_date(DateTimeUtils.getCurrentTime());
            mInfoSite.setRemote_name(txt_userAct_remoteName.getText().toString().trim());
            infoSiteAdapter.update(mInfoSite);

            ArrayList<MasterInfoSite> al_verifyInfoSite = new InfoSiteAdapter(UserActivity.this)
                    .load_site(preference.getCustID(), preference.getUn());

            if (al_verifyInfoSite.size() > 0){
                transHistoryUser(al_verifyInfoSite.get(0).getId_site(), getString(R.string.infoSite_trans));
                jobDescTrans(al_verifyInfoSite.get(0).getId_site());
            }else{
                messageUtils.snackBar_message("Mohon hubungi Support team", UserActivity.this, ConfigApps.SNACKBAR_WITH_BUTTON);
            }
        }catch (Exception e){
            String custName = txt_userAct_customer.getText().toString().trim();
            if (custName.contains(" ")){
                String[] split = custName.split(" ");
                custName = split[0]+""+split[1];
            }
            try {
                MasterInfoSite mInfoSite = new MasterInfoSite();
                mInfoSite.setCustomer_name(txt_userAct_customer.getText().toString().trim());
                mInfoSite.setRemote_address(txt_userAct_remoteAddress.getText().toString().trim());
                mInfoSite.setCity(txt_userAct_city.getText().toString().trim());
                mInfoSite.setKabupaten(txt_userAct_kabupaten.getText().toString().trim());
                mInfoSite.setProv(txt_userAct_proviency.getText().toString().trim());
                mInfoSite.setLat(txt_userAct_lat.getText().toString().trim());
                mInfoSite.setLongitude(txt_userAct_long.getText().toString().trim());
                mInfoSite.setProgress_type(selectedConnType.trim());
                mInfoSite.setTtwo(txt_userAct_ttwo.getText().toString().trim());
                mInfoSite.setUn_user(preference.getUn().trim());
                mInfoSite.setLocation_name(txt_userAct_locationName.getText().toString().trim());
                mInfoSite.setInsert_date(DateTimeUtils.getCurrentTime());
                mInfoSite.setRemote_name(txt_userAct_remoteName.getText().toString().trim());
                infoSiteAdapter.create(mInfoSite);

                ArrayList<MasterInfoSite> al_maxSite = new InfoSiteAdapter(UserActivity.this)
                        .loadMaxsite(preference.getUn().trim());

                if (al_maxSite.size() > 0){
                    preference.saveCustId(al_maxSite.get(0).getId_site(), custName);
                    preference.saveProgress(selectedConnType.trim());
                    transHistoryUser(al_maxSite.get(0).getId_site(), getString(R.string.infoSite_trans));
                    jobDescTrans(al_maxSite.get(0).getId_site());
                }else{ messageUtils.snackBar_message("Mohon hubungi Support team", UserActivity.this, ConfigApps.SNACKBAR_WITH_BUTTON); }
            }catch (Exception e2){ messageUtils.toastMessage("err insert info" +e2.toString(), ConfigApps.T_ERROR); }
        }
        finish();
    }
    private void jobDescTrans(int custId){
        try{
            MasterJobDesc mJobDesc = jobDescAdapter.queryForEq("id_site", preference.getCustID()).get(0);
            mJobDesc.setName_user(txt_userAct_name.getText().toString().trim());
            mJobDesc.setUser_phone(txt_userAct_phone.getText().toString().trim());
            mJobDesc.setName_pic(txt_userAct_name_pic.getText().toString().trim());
            mJobDesc.setJabatan_desc(txt_userAct_jabatan.getText().toString().trim());
            mJobDesc.setPic_phone(txt_userAct_picPhone.getText().toString().trim());
            mJobDesc.setProgress_type(selectedConnType.trim());
            mJobDesc.setId_site(custId);
            mJobDesc.setUpdate_date(DateTimeUtils.getCurrentTime());
            jobDescAdapter.update(mJobDesc);

            transHistoryUser(custId, getString(R.string.jobDesc_trans));
            messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
            emptyUserText();
            JobReportUtils.hideKeyboard(UserActivity.this);
        }catch (Exception e){
            try{
                MasterJobDesc mJobDesc = new MasterJobDesc();
                mJobDesc.setName_user(txt_userAct_name.getText().toString().trim());
                mJobDesc.setUser_phone(txt_userAct_phone.getText().toString().trim());
                mJobDesc.setName_pic(txt_userAct_name_pic.getText().toString().trim());
                mJobDesc.setJabatan_desc(txt_userAct_jabatan.getText().toString().trim());
                mJobDesc.setPic_phone(txt_userAct_picPhone.getText().toString().trim());
                mJobDesc.setProgress_type(selectedConnType.trim());
                mJobDesc.setId_site(custId);
                mJobDesc.setUn_user(preference.getUn());
                mJobDesc.setInsert_date(DateTimeUtils.getCurrentTime());

                jobDescAdapter.create(mJobDesc);
                transHistoryUser(custId, getString(R.string.jobDesc_trans));
                messageUtils.toastMessage(getString(R.string.transaction_success), ConfigApps.T_SUCCESS);
                emptyUserText();
                JobReportUtils.hideKeyboard(UserActivity.this);
            }catch (Exception e2){messageUtils.toastMessage("err insert job desc" +e2.toString(), ConfigApps.T_ERROR); }
        }
    }

    private void transHistoryUser(int custId, String transStep){
        ArrayList<MasterTransHistory> al_valTrans = new TransHistoryAdapter(getApplicationContext())
                .val_trans(custId, preference.getUn(), transStep);

        if (al_valTrans.size() > 0){
            try{
                MasterTransHistory mHist = transHistAdapter.queryForId(al_valTrans.get(0).getId_trans());
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(transStep);
                mHist.setUpdate_date(DateTimeUtils.getCurrentTime());
                mHist.setIs_submited(0);

                transHistAdapter.update(mHist);
            }catch (Exception e){ messageUtils.toastMessage("err trans Hist 1 " +e.toString(), ConfigApps.T_ERROR); }
        }else{
            try{
                MasterTransHistory mHist = new MasterTransHistory();
                mHist.setId_site(custId);
                mHist.setUn_user(preference.getUn());
                mHist.setInsert_date(DateTimeUtils.getCurrentTime());
                mHist.setTrans_step(transStep);
                mHist.setIs_submited(0);

                transHistAdapter.create(mHist);
            }catch (Exception e){messageUtils.toastMessage("err trans Hist 2 " +e.toString(), ConfigApps.T_ERROR); }
        }
    }

    private void emptyUserText(){
        txt_userAct_name.setText("");
        txt_userAct_service.setText("");
        txt_userAct_phone.setText("");
        txt_userAct_name_pic.setText("");
        txt_userAct_jabatan.setText("");
        txt_userAct_picPhone.setText("");
        txt_userAct_locationName.setText("");
        txt_userAct_customer.setText("");
        txt_userAct_remoteAddress.setText("");
        txt_userAct_remoteName.setText("");
        txt_userAct_city.setText("");
        txt_userAct_kabupaten.setText("");
        txt_userAct_proviency.setText("");
        txt_userAct_ttwo.setText("");
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
            String stringLat,stringLong;
            for (Location location : locationResult.getLocations()) {
                mLastLocation = location;
                txt_userAct_lat.setText(String.valueOf(mLastLocation.getLatitude()));
                txt_userAct_long.setText(String.valueOf(mLastLocation.getLongitude()));
                JobReportUtils jobUtils = new JobReportUtils();
                String convertCoor = jobUtils.convertCoordinat(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                messageUtils.toastMessage(" convert " +convertCoor, ConfigApps.T_INFO);
            }
        }
    };

    private void getUserValidation(){
        awesomeValidation.addValidation(this,R.id.txt_userAct_phone, Patterns.PHONE, R.string.phone_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_picPhone, Patterns.PHONE, R.string.phone_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_name_pic, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_jabatan, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_locationName, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_customer, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_city, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_kabupaten, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
        awesomeValidation.addValidation(this,R.id.txt_userAct_proviency, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
            case R.id.edit_menu :
                imgB_userAct_submit.setVisibility(View.VISIBLE);
                imgB_userAct_cancel.setVisibility(View.VISIBLE);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onPause(){
        try{
            if (mGoogleApiClient != null  &&  mGoogleApiClient.isConnected()) {
                mFusedLocation.removeLocationUpdates(callback);
                mGoogleApiClient.disconnect();
            }
        }catch (Exception e){}
        super.onPause();
    }
}
