package com.f1mert.lib.permission.helper;

import android.app.Activity;

public class LowApiPermissionHelper extends PermissionHelper {
    public LowApiPermissionHelper(Activity activity) {
        super(activity);
    }

    @Override
    public void reqeustPermissions(int requestCode, String... parms) {
        throw new IllegalStateException("低于6.0不需要");
    }

    @Override
    protected boolean shouldShowRequestPermissionRationale(String deniedPermission) {
        return false;
    }
}
