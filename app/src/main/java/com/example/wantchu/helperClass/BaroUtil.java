package com.example.wantchu.helperClass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;
import com.example.wantchu.Login;

public class BaroUtil {
    static SharedPreferences sf;
    public static boolean loginCheck(final Activity activity){
        Log.e("loginCheck" , " / loginCheck!");
        sf = activity.getPreferences(Context.MODE_PRIVATE);
        String nick = sf.getString("nick","");
        if(nick == null || nick.equals("")){
            new AlertDialog.Builder(activity)
                .setTitle("알 림")
                .setMessage("로그인이 필요한 페이지 입니다 \n로그인 하시겠습니까?")
                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.startActivity(new Intent(activity, Login.class));
                    activity.overridePendingTransition(0, 0);
                }
            }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .show();
            return false;
        }
        return true;
    }
    public static void showCustomDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("인터넷이 연결되어있는지 확인해주세요")
            .setCancelable(false)
            .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).show();
    }
}
