package com.example.wantchu.helperClass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Dialogs.AddFavoriteDialog;
import com.example.wantchu.Dialogs.HistoryDetailDialog;
import com.example.wantchu.Dialogs.NeedLoginDialog;
import com.example.wantchu.Login;
import com.example.wantchu.StoreInfoReNewer;

public class BaroUtil {
    static SharedPreferences sf;
    public static boolean loginCheck(final Activity activity){
        Log.e("loginCheck" , " / loginCheck!");
        SessionManager sm = new SessionManager(activity, SessionManager.SESSION_USERSESSION);
        SharedPreferences sf = sm.getUsersDetailSession();
        String nick = sf.getString(SessionManager.KEY_USERNAME,"");
        if(nick == null || nick.equals("")){
            NeedLoginDialog needLoginDialog = new NeedLoginDialog(activity);
            needLoginDialog.callFunction();


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
    public static boolean checkGPS(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Log.e("manager_state", manager.isProviderEnabled(LocationManager.GPS_PROVIDER)+"");
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}
