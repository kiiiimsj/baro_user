package com.example.wantchu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wantchu.helperClass.myGPSListener;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class FirstPage extends AppCompatActivity {

    private static int SLIDE_TIMER = 5000;

    ImageView backgroundImage;
    TextView wantchu;

    Animation sideAnim, bottomAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        backgroundImage = findViewById(R.id.first_background_image);
        wantchu = findViewById(R.id.first_textView);

        sideAnim = AnimationUtils.loadAnimation(this, R.anim.side_anim);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

        backgroundImage.setAnimation(sideAnim);
        wantchu.setAnimation(bottomAnim);

        SharedPreferences shf = this.getSharedPreferences("basketList", MODE_PRIVATE);
        SharedPreferences.Editor editor = shf.edit();
        editor.clear().commit();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstPage.this, MainPage.class);
                startActivity(intent);
                finish();
            }
        }, SLIDE_TIMER);
    }

//    private void startLocationService() {
//        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
//            return;
//        }
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        long minTime = 10000;
//        float minDistance = 0;
//        myGPSListener gpsListener = new myGPSListener(this);
//        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, gpsListener);
//        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,minTime,minDistance, gpsListener);
//        manager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,minTime,minDistance,gpsListener);
//    }
}