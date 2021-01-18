package com.tpn.baro.helperClass;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;

import com.tpn.baro.FirstPage;

public class PermissionHandler {
    private static final int PERMISSION_CODE = 0;

    private static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };
    public static boolean checkPermissions(Activity activity) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
    public static void requestPermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CODE);
    }
    public static AlertDialog.Builder needPermissionDialog(final Activity activity){
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("설정");
        builder.setCancelable(true);
        builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+activity.getPackageName()));
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }
        });
        return builder;
    }
//    public static boolean shouldShowRequestPermissionRationale(Activity activity) {
//        for (String permission : permissions) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
