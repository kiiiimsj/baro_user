package com.tpn.baro.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.tpn.baro.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    public FirebaseMessagingService() {
    }

    private static String CHANNEL_ID = "2";
    private static String CHANNEL_NAME = "channel1";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        showNotification(remoteMessage);
    }


    private void showNotification(RemoteMessage remoteMessage) {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH));
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            builder = new NotificationCompat.Builder(this);
        }
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(remoteMessage.getData().get("body"));
        style.setBigContentTitle(remoteMessage.getData().get("title"));
        builder.setSmallIcon(R.drawable.and_app_icon)
        .setContentTitle(remoteMessage.getData().get("title"))
        .setContentText(remoteMessage.getData().get("body"))
        .setStyle(style);
        Notification notification = builder.build();

        manager.notify(1, notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.i("FIREBASE New Token : ", token);
    }

}