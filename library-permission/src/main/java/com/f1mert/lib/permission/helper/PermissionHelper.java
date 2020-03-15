package com.f1mert.lib.permission.helper;

import android.app.Activity;
import android.os.Build;

import java.util.List;

//抽象辅助类
public abstract class PermissionHelper {

    private Activity activity;

    public PermissionHelper(Activity activity) {
        this.activity = activity;
    }

    public Activity getHost() {
        return activity;
    }

    public static PermissionHelper newInstance(Activity activity) {
        if(Build.VERSION.SDK_INT <Build.VERSION_CODES.M){
            return new LowApiPermissionHelper(activity);
        }
        return new ActivityPermissionHelper(activity);
    }

    /**
     * 用户权限 申请
     * @param requestCode 请求标识码(<256)
     * @param parms 需要授权的一组权限
     */
    public abstract void reqeustPermissions(int requestCode, String... parms);

    public boolean somePermissionPermanentlyDenied(List<String> deniedPermissions){
        for(String deniedPermission : deniedPermissions){
            if(!shouldShowRequestPermissionRationale(deniedPermission)){
                return true;
            }
        }
        return false;
    }

    /**
     * 第一次 打开app时, false
     * 上次弹出权限请求点击了拒绝,但没有勾选 不再询问 true
     * 上次弹出权限请求点击了拒绝，并且勾选了‘不再询问’false
     * @param deniedPermission
     * @return
     */
    protected abstract boolean shouldShowRequestPermissionRationale(String deniedPermission);
}
