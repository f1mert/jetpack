### 集成BasePermissionActivity
```
public abstract class BasePermissionActivity extends Activity implements PermissionCallBack {
    @Override
    public void onPermissionGranted(int requestCode, List<String> parms) {
        //权限通过
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

```
### 调用权限申请
```
@IPermission(LOCATION_REQUEST_CODE)
    private void locationTask(){
        String[] parms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS};
        if(PermissionManager.hasPermissions(this, parms)){
            Toast.makeText(this,"已经有了位置权限",Toast.LENGTH_SHORT).show();
        }else{
            PermissionManager.requestPermissions(this,LOCATION_REQUEST_CODE,parms);
        }
    }
```
