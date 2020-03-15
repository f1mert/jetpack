package com.f1mert.lib.permission;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.f1mert.lib.permission.dialog.AppSettingDialog;
import com.f1mert.lib.permission.listener.PermissionCallBack;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class BasePermissionActivity extends Activity implements PermissionCallBack {
    //所有权限都通过由主类处理
    //权限通过(有权限被通过处理)
    @Override
    public void onPermissionGranted(int requestCode, List<String> parms) {
    }

    @Override
    //权限被拒绝
    public void onPermissionDenied(int requestCode, List<String> parms) {
        //用户点击了拒绝，有可能勾选了‘不再循环’
        if (PermissionManager.somePermissionPermanentlyDenied(this, parms)) {
            new AppSettingDialog.Builder(this)
                    .setListener(new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).build()
                    .show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //权限申请回调
        PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从设置返回
    }

}
