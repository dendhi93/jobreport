package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
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

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
                // connected to wifi
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to mobile data
                return true;
            }
        }

        return false;
    }

}
