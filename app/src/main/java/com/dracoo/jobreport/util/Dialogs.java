package com.dracoo.jobreport.util;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.dracoo.jobreport.R;


public class Dialogs {

    public static void showDialog(Handler handler, final Activity activity, final String title, final String message, final Boolean isFinish) {

        try {
            if (activity != null) {
                if (!activity.isFinishing()) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(activity)
                                    .setTitle(title)
                                    .setMessage(message)
                                    .setIcon(R.drawable.ic_check)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (isFinish) {
                                                activity.finish();
                                            }
                                        }
                                    })
                                    .show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("###",""+e.toString());
        }
    }

    public static void showDialog(final Activity activity, final String title, final String message) {
        if (!activity.isFinishing()) {
            new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setIcon(R.drawable.ic_check)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }
}
