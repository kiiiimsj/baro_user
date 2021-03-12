package com.tpn.baro;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.PermissionHandler;

import org.jetbrains.annotations.NotNull;

public class FirstPage extends AppCompatActivity implements AutoPermissionsListener {
    TextView orderText;
    TextView getText;
    TextView baroLogo;
    private boolean firstflag = false;

    Animation sideLeftAnim, bottomAnim, rotated_35, rotated_35_to_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(FirstPage.this, this.toString());
        }
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_first_page);

        DisplayMetrics metrics = getApplicationContext().getResources().getDisplayMetrics();
        int screenHeight = metrics.heightPixels;
        Log.e("getBottomHeight", getNavigationBarHeight(this)+"");
        Log.e("WindowHeight", screenHeight+"");

        //640x360
        //window - bottom navigation bar size height
//        E/WindowHeight: 592
        //bottom navigation bar size height
//        E/getBottomHeight: 48

        //3040×1440
        //window - bottom navigation bar size height
//        E/WindowHeight: 2759
        //bottom navigation bar size height
//        E/getBottomHeight: 281

        SharedPreferences shf = this.getSharedPreferences("basketList", MODE_PRIVATE);
        SharedPreferences.Editor editor = shf.edit();
        editor.clear().commit();

        orderText = findViewById(R.id.order_text);
        getText = findViewById(R.id.get_text);
        baroLogo =findViewById(R.id.menu_image);

        sideLeftAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_left);
        rotated_35 = AnimationUtils.loadAnimation(this, R.anim.anim_rotated_35);
        rotated_35_to_0 = AnimationUtils.loadAnimation(this, R.anim.anim_rotated_35_to_0);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.anim_up);

        getText.setVisibility(View.INVISIBLE);
        baroLogo.setVisibility(View.INVISIBLE);
        orderText.setAnimation(sideLeftAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                getText.setVisibility(View.VISIBLE);
                getText.setAnimation(rotated_35);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        baroLogo.setVisibility(View.VISIBLE);
                        baroLogo.startAnimation(bottomAnim);
                        getText.startAnimation(rotated_35_to_0);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                AutoPermissions.Companion.loadAllPermissions(FirstPage.this, 101);
//                                Intent intent = new Intent(FirstPage.this, MainPage.class);
//                                startActivity(intent);
//                                finish();
//                                final AlertDialog.Builder builder = new AlertDialog.Builder(FirstPage.this);
//                                builder.setTitle("설정");
//                                builder.setCancelable(true);
//                                builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
//                                builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
////                                        dialog.dismiss();
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+getPackageName()));
//                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                    }
//                                });
//                                builder.show();
                            }
                        }, 800);
                    }
                }, 800);
            }
        }, 800);
    }
    public static int getNavigationBarHeight(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;

            ((Activity)context).getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;

            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }
    public void clarifyPermission(){
        if (!PermissionHandler.checkPermissions(this)){
//            PermissionHandler.requestPermission(this);
            AlertDialog.Builder builder = PermissionHandler.needPermissionDialog(this);
            builder.show();
        }else{
//            Intent intent = new Intent(FirstPage.this, MainPage.class);
            Intent intent = new Intent(FirstPage.this, NewMainPage.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (firstflag) {
            clarifyPermission();
        }else{
            firstflag = true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
        clarifyPermission();
//        int locationPermission = ContextCompat.checkSelfPermission(this,   Manifest.permission.ACCESS_FINE_LOCATION);
//        int storagePermission = ContextCompat.checkSelfPermission(this,   Manifest.permission.ACCESS_FINE_LOCATION);
//        if(locationPermission == PackageManager.PERMISSION_DENIED){
//            // 권한 없음
//            ActivityCompat.requestPermissions(this,
//                    new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
//                    1000);
//        }
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {

    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {

    }
}