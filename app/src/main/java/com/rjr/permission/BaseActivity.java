package com.rjr.permission;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.rjr.permission.utils.PermissionUtil;

/**
 *
 * Created by Administrator on 2018/5/4.
 */

public class BaseActivity extends AppCompatActivity {

    protected Activity mContext = this;

    private PermissionUtil.PermissionListener mListener;

    protected void requestRunPermissions(Activity activity, String[] permissions, PermissionUtil.PermissionListener listener) {
        mListener = listener;
        PermissionUtil.requestRunPermissions(activity, permissions, listener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestPermissionsResult(requestCode, permissions, grantResults, mListener);
    }
}
