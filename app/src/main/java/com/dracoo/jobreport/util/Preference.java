package com.dracoo.jobreport.util;

import android.content.Context;
import android.content.SharedPreferences;

public class Preference {
    private static final String PREF_NAME = "ReportSharedPreference";
    int PRIVATE_MODE = 0;
    public static final String KEY_USERNAME = "usernama";
    public static final String KEY_SERVICEPOINT = "service_points";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_PROGRESS_TYPE = "progress_type";
    public static final String KEY_CUST_ID = "cust_id";
    public static final String KEY_CUST_NAME = "cust_name";
    public static final String KEY_CONNECTION_TYPE = "Connection_type";


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;

    public Preference(Context con) {
        this.context = con;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void saveUn(String un, String sp, String phone){
        editor.putString(KEY_USERNAME, un);
        editor.putString(KEY_SERVICEPOINT, sp);
        editor.putString(KEY_PHONE, phone);

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

    public void saveConnection(String connectionype){
        editor.putString(KEY_CONNECTION_TYPE, connectionype);
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

    //after submit
    public void clearDataTrans(){
        editor.remove(KEY_CUST_ID);
        editor.remove(KEY_PROGRESS_TYPE);
        editor.remove(KEY_CONNECTION_TYPE);
        editor.remove(KEY_CUST_NAME);

        editor.commit();
    }
    public void clearPreference(){
        editor.clear();
        editor.apply();
    }
}
