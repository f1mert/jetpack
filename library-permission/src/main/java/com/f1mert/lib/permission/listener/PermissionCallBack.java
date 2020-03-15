package com.f1mert.lib.permission.listener;

import java.util.List;

public interface PermissionCallBack {

    public void onPermissionDenied(int requestCode,List<String> parms);

    public void onPermissionGranted(int requestCode,List<String> parms);

}
