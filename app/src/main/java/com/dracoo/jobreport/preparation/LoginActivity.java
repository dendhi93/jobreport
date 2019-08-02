package com.dracoo.jobreport.preparation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.dracoo.jobreport.BuildConfig;
import com.dracoo.jobreport.R;
import com.dracoo.jobreport.database.adapter.UserAccessAdapter;
import com.dracoo.jobreport.database.master.MasterUserAccess;
import com.dracoo.jobreport.feature.MenuActivity;
import com.dracoo.jobreport.util.ConfigApps;
import com.dracoo.jobreport.util.DateTimeUtils;
import com.dracoo.jobreport.util.MessageUtils;
import com.dracoo.jobreport.util.Preference;
import com.j256.ormlite.dao.Dao;

import org.w3c.dom.Text;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.txt_login_un)
    EditText txt_login_un;
    @BindView(R.id.txt_login_servicePoint)
    EditText txt_login_servicePoint;
    @BindView(R.id.txt_login_handphone)
    EditText txt_login_handphone;
    @BindView(R.id.lbl_login_version)
    TextView lbl_login_version;
    @BindView(R.id.txt_login_pass)
    TextView txt_login_pass;

    private MessageUtils messageUtils;
    private AwesomeValidation awesomeValidation;
    private Preference preference;
    private EditText txt_alert_un,txt_alert_pass, txt_alert_name;
    private Dao<MasterUserAccess, Integer> userAccessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        try{
            userAccessAdapter = new UserAccessAdapter(getApplicationContext()).getAdapter();
        }catch (Exception e){}
    }

    @Override
    protected void onStart(){
        super.onStart();

        messageUtils = new MessageUtils(LoginActivity.this);
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);
        getValidation();

        lbl_login_version.setText(BuildConfig.VERSION_NAME);
        preference = new Preference(LoginActivity.this);

    }

    public void actClick(View view){
        final String un = txt_login_un.getText().toString().trim();
        final String servicePoint = txt_login_servicePoint.getText().toString().trim();
        final String handphone = txt_login_handphone.getText().toString().trim();

        if(un.equals("") || servicePoint.equals("") || handphone.equals("")){
            messageUtils.snackBar_message(getString(R.string.emptyString),
                    LoginActivity.this, ConfigApps.SNACKBAR_NO_BUTTON);
        } else if (handphone.length() < 10){
            messageUtils.snackBar_message("No handphone kurang dari 10 angka", LoginActivity.this, ConfigApps.SNACKBAR_WITH_BUTTON);
        } else if(awesomeValidation.validate()){
            boolean valUn = new UserAccessAdapter(getApplicationContext())
                    .valLogin(txt_login_un.getText().toString().trim(), txt_login_pass.getText().toString().trim());
            if(!valUn){
                messageUtils.toastMessage("username atau password tidak valid", ConfigApps.T_WARNING);
            }else{
                if (txt_login_un.getText().toString().trim().equals("admin")){
                    displayAlert();
                } else if (!preference.getUn().equals("") && !preference.getUn().equals(un.trim())){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("Username sebelumnya adalah " +preference.getUn()+"\nApakah anda ingin ganti user ?")
                            .setIcon(R.drawable.ic_check)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    preference.clearPreference();
                                    saveloginPref(un, servicePoint, handphone);
                                }
                            })
                            .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }else if (!preference.getServicePoint().equals("") && !preference.getServicePoint().equals(txt_login_servicePoint.getText().toString().trim())){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("Service Poin sebelumnya adalah " +preference.getServicePoint()+"\nApakah anda ingin update service point ?")
                            .setIcon(R.drawable.ic_check)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveloginPref(un, servicePoint, handphone);
                                }
                            })
                            .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }else if (!preference.getPhone().equals("") && !preference.getPhone().equals(txt_login_handphone.getText().toString().trim())){
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Warning")
                            .setMessage("Handphone sebelumnya adalah " +preference.getPhone()+"\nApakah anda ingin ganti user ?")
                            .setIcon(R.drawable.ic_check)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveloginPref(un, servicePoint, handphone);
                                }
                            })
                            .setNegativeButton("Cancel",  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else{
                    saveloginPref(un, servicePoint, handphone);
                }
            }
        }
    }

    private void getValidation(){
        awesomeValidation.addValidation(this,R.id.txt_login_handphone, Patterns.PHONE, R.string.phone_validation);
//        awesomeValidation.addValidation(this,R.id.txt_login_un, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.name_validation);
    }

    private void saveloginPref(String user, String servicePoint, String userHandphone){
        ArrayList<MasterUserAccess> alUser = new UserAccessAdapter(getApplicationContext())
                .userList(txt_login_un.getText().toString().trim(), txt_login_pass.getText().toString().trim());
        String techName;
        if (alUser.size() > 0){ techName = alUser.get(0).getUa_name();
        }else{ techName = "FULAN"; }
        preference.saveUn(user,servicePoint, userHandphone, techName);
        Intent intent = new Intent(LoginActivity.this, MenuActivity.class);
        intent.putExtra(MenuActivity.EXTRA_CALLER_ACTIVITY, MenuActivity.EXTRA_FLAG_DASH);
        startActivity(intent);
        finish();
    }

    private void displayAlert(){
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View alertView = inflater.inflate(R.layout.alert_add_user, null);
        alert.setView(alertView);
        txt_alert_un    = alertView.findViewById(R.id.txt_alert_un);
        txt_alert_pass    = alertView.findViewById(R.id.txt_alert_pass);
        txt_alert_name  = alertView.findViewById(R.id.txt_alert_name);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (txt_alert_un.getText().toString().trim().equals("")
                        || txt_alert_pass.getText().toString().trim().equals("") ||
                        txt_alert_name.getText().toString().trim().equals("")){
                    messageUtils.toastMessage(getString(R.string.emptyString), ConfigApps.T_WARNING);
                    displayAlert();
                }else{
                    try{
                        ArrayList<MasterUserAccess> alValUser = new UserAccessAdapter(LoginActivity.this)
                                .valUserAccess(txt_alert_un.getText().toString().trim());
                        if (alValUser.size() > 0) {
                            if (txt_alert_un.getText().toString().trim().equals("admin")){
                                messageUtils.toastMessage("User admin tidak bisa diubah password", ConfigApps.T_WARNING);
                            }else{
                                MasterUserAccess mUserAccess = userAccessAdapter.queryForId(alValUser.get(0).getId_user_list());
                                mUserAccess.setUa_password(txt_alert_pass.getText().toString().trim());
                                mUserAccess.setUa_name(txt_alert_name.getText().toString().trim());
                                mUserAccess.setUa_update_date(DateTimeUtils.getCurrentTime().trim());
                                userAccessAdapter.update(mUserAccess);
                                messageUtils.toastMessage("ubah password success",ConfigApps.T_SUCCESS);
                                setEmptyText();
                            }
                        }else{
                            //ADD USER
                            MasterUserAccess mUserAccess = new MasterUserAccess();
                            mUserAccess.setUa_username(txt_alert_un.getText().toString().trim());
                            mUserAccess.setUa_password(txt_alert_pass.getText().toString().trim());
                            mUserAccess.setUa_name(txt_alert_name.getText().toString().trim());
                            mUserAccess.setUa_insert_date(DateTimeUtils.getCurrentTime().trim());
                            userAccessAdapter.create(mUserAccess);
                            messageUtils.toastMessage("Add user Success",ConfigApps.T_SUCCESS);
                            setEmptyText();
                        }
                    }catch (Exception e){ messageUtils.toastMessage("failed to save user " +e.toString(), ConfigApps.T_ERROR); }
                    dialog.dismiss();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    private void setEmptyText(){
        txt_login_un.setText("");
        txt_login_pass.setText("");
        txt_login_servicePoint.setText("");
        txt_login_handphone.setText("");
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        LoginActivity.this.finish();
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        LoginActivity.this.startActivity(i);
        finish();
    }
}
