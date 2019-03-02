package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.sdsmdg.tastytoast.TastyToast;

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
    
}
