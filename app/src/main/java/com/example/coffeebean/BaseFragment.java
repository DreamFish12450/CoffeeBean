package com.example.coffeebean;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected Activity mContext;
    public static final int REQUEST_CALL_PERMISSION = 10111; //拨号请求码\
    private String phonenum="";
    /**
     * 申请指定的权限.
     */
    public void requestPermission(int code, String... permissions) {

        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(permissions, code);
        }
    }

    /**
     * 判断是否有某项权限
     * @param string_permission 权限
     * @param request_code 请求码
     * @return
     */
    public boolean checkReadPermission(String string_permission,int request_code) {
        boolean flag = false;
        if (ContextCompat.checkSelfPermission(mContext, string_permission) == PackageManager.PERMISSION_GRANTED) {//已有权限
            flag = true;
        } else {//申请权限
            ActivityCompat.requestPermissions(mContext, new String[]{string_permission}, request_code);
        }
        return flag;
    }

    /**
     * 检查权限后的回调
     * @param requestCode 请求码
     * @param permissions  权限
     * @param grantResults 结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CALL_PERMISSION: //拨打电话
                if (permissions.length != 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {//失败
                    Toast.makeText(mContext,"请允许拨号权限后再试",Toast.LENGTH_SHORT).show();
                } else {//成功
                    call("tel:"+phonenum);
                }
                break;
        }
    }
    /**
     * 拨打电话（直接拨打）
     * @param telPhone 电话
     */
    public void call(String telPhone) {
        phonenum=telPhone;
        if (checkReadPermission(Manifest.permission.CALL_PHONE, REQUEST_CALL_PERMISSION)) {
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(telPhone));
            startActivity(intent);
        }
    }



}