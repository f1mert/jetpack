package com.f1mert.lib.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import com.f1mert.lib.permission.annotation.IPermission;
import com.f1mert.lib.permission.helper.PermissionHelper;
import com.f1mert.lib.permission.listener.PermissionCallBack;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

public class PermissionManager {
    /**
     * 检查所请求的权限是否被授予
     * @param activity
     * @param parms
     * @return
     */
    public static boolean hasPermissions(@NonNull Activity activity, String... parms){
        //如果低于6.0，无需做权限判断
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        for(String parm:parms){
            //任意一个权限被拒绝返回false
            if(ContextCompat.checkSelfPermission(activity,parm) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * 向用户申请权限
     * @param activity
     * @param requestCode
     * @param parms
     */
    public static void requestPermissions(@NonNull Activity activity,int requestCode,@NonNull String... parms){
        //请求之前 ,做一次检查
        if(hasPermissions(activity,parms)){
            notifyHasPermissions(activity,requestCode,parms);
            return;
        }
        //权限申请
        PermissionHelper helper = PermissionHelper.newInstance(activity);
        helper.reqeustPermissions(requestCode,parms);
    }

    private static void notifyHasPermissions(Activity activity, int requestCode, String[] parms) {
        //将授权通过的权限组转参告知处理权限结果方法
        int[] grantResults = new int[parms.length];
        for(int i=0;i<parms.length;i++){
            grantResults[i] = PackageManager.PERMISSION_GRANTED;//全部通过
        }
        onRequestPermissionsResult(requestCode,parms,grantResults,activity);
    }

    public static void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults, Activity activity) {
        List<String> granted = new ArrayList<>();
        List<String> denied = new ArrayList<>();
        for(int i = 0;i<permissions.length;i++){
            String perm = permissions[i];
            if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                granted.add(perm);
            }else{
                denied.add(perm);
            }
        }

        if(!granted.isEmpty()){
            if(activity instanceof PermissionCallBack){
                ((PermissionCallBack) activity).onPermissionGranted(requestCode,granted);
            }
        }

        if(!denied.isEmpty()){
            if(activity instanceof PermissionCallBack){
                ((PermissionCallBack) activity).onPermissionDenied(requestCode,denied);
            }
        }

        //全部通过,通过反射注解调用权限方法
        if(!granted.isEmpty()&&denied.isEmpty()){
            reflectAnnotationMethod(activity,requestCode);
        }
    }

    private static void reflectAnnotationMethod(Activity activity, int requestCode) {

        Class<? extends Activity> clazz = activity.getClass();
        //获取类的所有方法
        Method[] methods = clazz.getDeclaredMethods();
        //遍历所有方法
        for(Method method:methods){
            //判断是不是Ipermission注解
            if(method.isAnnotationPresent(IPermission.class)){
                IPermission iPermission = method.getAnnotation(IPermission.class);
                //如果注解的值等于请求标识码(两次匹配，避免冲突)
                if(iPermission.value() == requestCode){
                    //严格校验
                    //方法返回须是void (三次匹配)
                    Type returnType = method.getGenericReturnType();
                    if(!"void".equals(returnType.toString())){
                        throw new RuntimeException(method.getName() + "方法返回必须是void");
                    }
                    //方法参数匹配(四次匹配)
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    if(parameterTypes.length > 0){
                        throw new RuntimeException(method.getName() + "方法无参数");
                    }
                    try {
                        if(!method.isAccessible())method.setAccessible(true);//当方法为私有，设置为可以访问
                        method.invoke(activity);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }


    public static boolean somePermissionPermanentlyDenied(Activity activity, List<String> deniedPermissions) {
        return PermissionHelper.newInstance(activity).somePermissionPermanentlyDenied(deniedPermissions);
    }
}
