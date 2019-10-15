package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.List;
import java.util.Locale;

public class MessageUtils {

    private static Context context;
    public MessageUtils(Context c) {
        context = c;
    }

    public void toastMessage(String message, int type) {
        TastyToast.makeText(context, message, TastyToast.LENGTH_LONG, type);
    }

    public void snackBar_message(String snackMessage, Activity activity, int action){
        View rootView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (action == 1){
            final Snackbar snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_INDEFINITE);
            snackBar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackBar.dismiss();
                }
            });
            snackBar.show();
        }else{
            final Snackbar snackBar = Snackbar.make(rootView, snackMessage, Snackbar.LENGTH_LONG);
            snackBar.show();
        }
    }

    public static boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null && mobileNetwork.isConnected()) {
            return true;
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    public String convertToAddress(Location location){
        try{
            Geocoder geo = new Geocoder(context, Locale.getDefault());
            if (location != null){
                List<Address> addresses = geo.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (!addresses.isEmpty()){
                    return addresses.get(0).getLocality();
                }
            }
        }catch (Exception e){ Log.d("###",""+e.toString()); }
        return "";
    }
}
