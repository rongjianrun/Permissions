package com.rjr.permission.utils;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * Created by Administrator on 2018/5/4.
 */

public class FileUtil {

    public static String getFilePath() {
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath();
            return path;
        } else {
        }
        return null;
    }

    public static File getFile() {
        if (TextUtils.equals(Environment.getExternalStorageState(), Environment.MEDIA_MOUNTED)) {
            File outputFile = new File(Environment.getExternalStorageDirectory(), "images" + File.separator + "test.png");
            Log.e("rong", "getFile: " + outputFile.exists());
//            if (!outputFile.exists()) {
//                outputFile.mkdirs();
//            }
            return outputFile;
        }
        return null;
    }
}
