package com.tpn.baro.helperClass;

import android.app.Activity;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Build;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import com.tpn.baro.Basket;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Dialogs.NeedLoginDialog;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.NewMainPage;
import com.tpn.baro.OrderDetails;
import com.tpn.baro.StoreInfoReNewer;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

public class BaroUtil {
    public interface ReloadActivity {
        void reload();
    }

    static SharedPreferences sf;
    public static boolean loginCheck(final Activity activity){
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
    public static boolean checkGPS(final Context context) {
        boolean isClose = false;
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("설정");
            builder.setCancelable(true);
            builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });
            builder.show();
            isClose = true;
        }else {
            isClose = false;
        }
        return isClose;
    }
    public static String pad(int fieldWidth, char padChar, String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length(); i < fieldWidth; i++) {
            sb.append(padChar);
        }
        sb.append(s);
        return sb.toString();
    }
    public boolean getActivityOnPause(Activity activity) {
        if(new TopBar().getTokenActivityName(activity.toString()).equals("NewMainPage")) {
            return NewMainPage.onPause;
        }
        if(new TopBar().getTokenActivityName(activity.toString()).equals("StoreInfoReNewer")) {
            return StoreInfoReNewer.onPause;
        }
        if(new TopBar().getTokenActivityName(activity.toString()).equals("OrderDetails")) {
            return OrderDetails.onPause;
        }
        if(new TopBar().getTokenActivityName(activity.toString()).equals("Basket")) {
            return Basket.onPause;
        }

        return false;
    }
    public boolean checkTopBarTimeThreadActivity(Activity activity) {
        switch (new TopBar().getTokenActivityName(activity.toString())) {
            case "NewMainPage":
            case "StoreInfoReNewer":
            case "OrderDetails":
            case "Basket":
                return true;
            default:
                return false;
        }
    }
    public void fifteenTimer(final TextView timerTextView, final Activity activity /*,final ReloadActivity reloadActivity*/) {
        if(!checkTopBarTimeThreadActivity(activity)) {
            return;
        }
        new Thread((new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                while (!getActivityOnPause(activity)) {
                    Calendar calendar = GregorianCalendar.getInstance();
                    String minuteString = BaroUtil.pad(2, '0', calendar.get(Calendar.MINUTE) + "");
                    String secondString = BaroUtil.pad(2, '0', calendar.get(Calendar.SECOND) +"");
                    Log.i("activity : ", activity.toString()+secondString);
                    try {
                        Thread.sleep(1000);
                        final int minuteFinal = 14 - (Integer.parseInt(minuteString) % 15);;
                        final int secondFinal = 59 - Integer.parseInt(secondString);

                        if(minuteFinal==0 && secondFinal == 1) {
                            /*reloadActivity.reload();*/
                            Log.e("refresh", true+"");
                            activity.finish();
                            activity.overridePendingTransition(0, 0);
                            activity.startActivity(activity.getIntent());
                            activity.overridePendingTransition(0, 0);
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timerTextView.setText(BaroUtil.pad(2,'0', minuteFinal+"")+":"+BaroUtil.pad(2, '0',secondFinal+""));
                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        })).start();
    }
}
