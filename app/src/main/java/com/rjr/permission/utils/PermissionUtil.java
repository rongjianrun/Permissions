package com.rjr.permission.utils;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 权限工具类
 * Created by Administrator on 2018/5/4.
 */

public class PermissionUtil {

    private static final int PERMISSION_REQUEST_CODE = 0xf100;

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions 权限数组
     * @param listener
     */
    public static void requestRunPermissions(Activity activity, String[] permissions, PermissionListener listener) {
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (!selfPermissionGranted(activity, permission)) {
                permissionList.add(permission);
            }
        }
        if (permissionList.isEmpty()) {
            // 表示全部授权了
            listener.onGranted();
        } else {
            // 权限请求
            ActivityCompat.requestPermissions(activity, permissionList.toArray(new String[permissionList.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * 判断该权限是否已授权
     *
     * @param activity
     * @param permission
     * @return true 已授权 false 没授权
     */
    private static boolean selfPermissionGranted(Activity activity, String permission) {
        boolean result = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            result = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
        }
        return result;
    }

    /**
     * BaseActivity调用
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public static void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, PermissionListener listener) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0) {
                return;
            }
            // 存放没授权的权限
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.isEmpty()) {
                // 说明都授权了
                listener.onGranted();
            } else {
                listener.onDenied(deniedPermissions);
            }
        }
    }

    /**
     * 打开权限设置页
     *
     * @param activity
     * @param message
     */
    public static void openSettingActivity(final Activity activity, String message) {
        showMessageOKCancel(activity, message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                activity.startActivity(intent);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(activity, "请设置必要的权限", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 显示弹框
     *
     * @param activity
     * @param message
     * @param settingListener
     * @param cancelListener
     */
    private static void showMessageOKCancel(Activity activity, String message,
                                            DialogInterface.OnClickListener settingListener,
                                            DialogInterface.OnClickListener cancelListener) {
        String permissionMessage = "当前应用缺少必要权限(" + message + ")\n" +
                "\n 请点击“设置”-“权限”-打开所需权限。\n" + "" +
                "\n 最后点击两次后退按钮，即可返回";
//        new AlertDialog
//                .Builder(activity)
//                .setTitle("提示")
//                .setMessage(permissionMessage)
//                .setNegativeButton("设置", settingListener)
//                .setNeutralButton("取消", cancelListener)
//                .show();
        Toast.makeText(activity, permissionMessage, Toast.LENGTH_SHORT).show();
    }

    public interface PermissionListener {

        void onGranted();

        void onDenied(List<String> permissions);
    }
}
