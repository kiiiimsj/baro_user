package com.tpn.baro.helperClass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class ThreadKillReceiver extends BroadcastReceiver {
    private String TAG = "ThreadKillReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG,TAG);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent in = new Intent(context, RestartService.class);
            context.startForegroundService(in);
        } else {
            Intent in = new Intent(context, OrderCancelIfNotAccept.class);
            context.startService(in);
        }

    }
}
