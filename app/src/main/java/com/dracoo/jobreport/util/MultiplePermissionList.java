package com.dracoo.jobreport.util;

import com.dracoo.jobreport.preparation.PermissionActivity;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class MultiplePermissionList implements MultiplePermissionsListener {

    private final PermissionActivity activity;


    public MultiplePermissionList(PermissionActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
        for (PermissionGrantedResponse response : multiplePermissionsReport.getGrantedPermissionResponses()) {
            activity.showPermissionGranted(response.getPermissionName());
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
        activity.showPermissionRationale(token);
    }
}
