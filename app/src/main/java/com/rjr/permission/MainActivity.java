package com.rjr.permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rjr.permission.utils.FileUtil;
import com.rjr.permission.utils.PermissionUtil;

import java.io.File;
import java.security.Permission;
import java.util.List;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
    }

    public void takePhoto(View view) {
        final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); // 启动系统相机
        Uri photoUri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            photoUri = FileProvider.getUriForFile(this, "com.rjr.permission.provider", FileUtil.getFile());
        } else {
            photoUri = Uri.fromFile(FileUtil.getFile()); // 传递路径
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    private void permission() {
        requestRunPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION}, new PermissionUtil.PermissionListener() {
            @Override
            public void onGranted() {
                // 权限已全部授权
                Toast.makeText(mContext, "权限已全部授权", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> permissions) {
                PermissionUtil.openSettingActivity(mContext, "相机、定位");
            }
        });
    }
}
