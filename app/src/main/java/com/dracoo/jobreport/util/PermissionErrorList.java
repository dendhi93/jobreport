package com.dracoo.jobreport.util;

import android.util.Log;

import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

public class PermissionErrorList implements PermissionRequestErrorListener {
    @Override
    public void onError(DexterError error) {
        Log.d("error Permission",""+error.toString());
    }
}
