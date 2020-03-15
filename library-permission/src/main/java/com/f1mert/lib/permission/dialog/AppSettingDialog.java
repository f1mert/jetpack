package com.f1mert.lib.permission.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;

import java.util.Locale;

import androidx.annotation.NonNull;

public class AppSettingDialog implements DialogInterface.OnClickListener {

    public static final int SETTING_CODE = 333;

    private Activity activity;
    private String title;
    private String message;
    private String positiveButton;
    private String negativeButton;
    private DialogInterface.OnClickListener listener;
    private int requestCode;

    public AppSettingDialog(Builder builder) {
        this.activity = builder.activity;
        this.title = builder.title;
        this.message = builder.message;
        this.positiveButton = builder.positiveButton;
        this.negativeButton = builder.negativeButton;
        this.listener = builder.listener;
        this.requestCode = builder.requestCode;
    }

    public void show(){
        if(listener != null){
            showDialog();
        }else{
            throw new IllegalArgumentException("对话监听框不能为空");
        }
    }

    private void showDialog() {
        new AlertDialog.Builder(activity)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButton,this)
                .setNegativeButton(negativeButton,listener)
                .create()
                .show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",activity.getPackageName(),null);
        intent.setData(uri);
        activity.startActivityForResult(intent,requestCode);

    }

    public static class Builder{
        private Activity activity;
        private String title;
        private String message;
        private String positiveButton;
        private String negativeButton;
        private DialogInterface.OnClickListener listener;
        private int requestCode = -1;

        public Builder(@NonNull Activity activity){this.activity = activity;}

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setListener(DialogInterface.OnClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setRequestCode(int requestCode) {
            this.requestCode = requestCode;
            return this;
        }

        public AppSettingDialog build(){
            this.title = "需要的授权";
            this.message = TextUtils.isEmpty(message)?"打开设置,启动权限":message;
            this.positiveButton = activity.getString(android.R.string.ok);
            this.negativeButton = activity.getString(android.R.string.cancel);
            this.requestCode = requestCode > 0 ? requestCode :SETTING_CODE;

            return new AppSettingDialog(this);
        }
    }


}
