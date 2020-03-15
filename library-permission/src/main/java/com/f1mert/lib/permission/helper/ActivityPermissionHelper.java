package com.f1mert.lib.permission.helper;

import android.app.Activity;

import androidx.core.app.ActivityCompat;

public class ActivityPermissionHelper extends PermissionHelper {
    public ActivityPermissionHelper(Activity activity) {
        super(activity);
    }

    @Override
    public void reqeustPermissions(int requestCode, String... parms) {
        ActivityCompat.requestPermissions(getHost(),parms,requestCode);
    }

    @Override
    protected boolean shouldShowRequestPermissionRationale(String deniedPermission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(getHost(),deniedPermission);
    }
}
