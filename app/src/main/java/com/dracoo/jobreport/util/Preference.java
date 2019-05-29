package com.dracoo.jobreport.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private static final String PREF_NAME = "ReportSharedPreference";
    int PRIVATE_MODE = 0;
    public static final String KEY_USERNAME = "usernama";
    public static final String KEY_SERVICEPOINT = "service_points";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_TECH_NAME = "tech_name";
    public static final String KEY_PROGRESS_TYPE = "progress_type";
    public static final String KEY_CUST_ID = "cust_id";
    public static final String KEY_CUST_NAME = "cust_name";
    public static final String KEY_CONNECTION_TYPE = "Connection_type";

    public static final String KEY_SEND_WA = "send_wa";
    public static final String KEY_SEND_EMAIL = "send_email";
    public static final String KEY_SEND_GFORM = "post_data_gform";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public Preference(Context con) {
        this.context = con;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUn(String un, String sp, String phone, String techName){
        editor.putString(KEY_USERNAME, un);
        editor.putString(KEY_SERVICEPOINT, sp);
        editor.putString(KEY_PHONE, phone);
        editor.putString(KEY_TECH_NAME, techName);

        editor.apply();
    }

    public void saveCustId(int custId, String custName){
        editor.putInt(KEY_CUST_ID, custId);
        editor.putString(KEY_CUST_NAME, custName);
        editor.apply();
    }

    public void saveProgress(String progressType){
        editor.putString(KEY_PROGRESS_TYPE, progressType);
        editor.apply();
    }

    public void saveConnection(String connectionType){
        editor.putString(KEY_CONNECTION_TYPE, connectionType);
        editor.apply();
    }

    public void saveSend(int sendType){
        if (sendType == ConfigApps.EMAIL_TYPE){
            editor.putInt(KEY_SEND_EMAIL, ConfigApps.SUBMIT_SEND);
        }else if (sendType == ConfigApps.WA_TYPE){
            editor.putInt(KEY_SEND_WA, ConfigApps.SUBMIT_SEND);
        }else if (sendType == ConfigApps.GFORM_TYPE){
            editor.putInt(KEY_SEND_GFORM, ConfigApps.SUBMIT_SEND);
        }
        editor.apply();
    }

    public String getUn(){
        return sharedPreferences.getString(KEY_USERNAME, "");
    }
    public String getServicePoint(){
        return sharedPreferences.getString(KEY_SERVICEPOINT, "");
    }
    public String getPhone(){
        return sharedPreferences.getString(KEY_PHONE, "");
    }
    public Integer getCustID(){
        return sharedPreferences.getInt(KEY_CUST_ID, 0);
    }
    public String getCustName(){return sharedPreferences.getString(KEY_CUST_NAME, "");}
    public String getProgress(){return sharedPreferences.getString(KEY_PROGRESS_TYPE, "");}
    public String getConnType(){return sharedPreferences.getString(KEY_CONNECTION_TYPE, "");}
    public Integer getSendEmail(){return sharedPreferences.getInt(KEY_SEND_EMAIL, 0);}
    public Integer getSendWA(){return sharedPreferences.getInt(KEY_SEND_WA, 0);}
    public String getTechName(){return sharedPreferences.getString(KEY_TECH_NAME, "");}

    //after submit
    public void clearDataTrans(){
        editor.remove(KEY_CUST_ID);
        editor.remove(KEY_PROGRESS_TYPE);
        editor.remove(KEY_CONNECTION_TYPE);
        editor.remove(KEY_CUST_NAME);
        editor.remove(KEY_SEND_WA);
        editor.remove(KEY_SEND_EMAIL);

        editor.apply();
    }
    public void clearPreference(){
        editor.clear();
        editor.apply();
    }
}
