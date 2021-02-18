package com.tpn.baro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import maes.tech.intentanim.CustomIntent;

public class GetPermissionBeforeRegister extends AppCompatActivity {
    CheckBox allCheck;
    CheckBox mapPermission;
    CheckBox storagePermission;
    CheckBox privacyPermission;
    CheckBox marketingSnsPermission;
    Button goNextBtn;

    SharedPreferences marketingSnsPermissionSave;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permissison_before_register);
        allCheck = findViewById(R.id.all_check);
        mapPermission = findViewById(R.id.map_permission);
        storagePermission = findViewById(R.id.storage_permission);
        privacyPermission = findViewById(R.id.user_privacy_permission);
        marketingSnsPermission = findViewById(R.id.marketing_sns_permission);
        goNextBtn = findViewById(R.id.login_button);

        marketingSnsPermissionSave = getSharedPreferences("marketingSnsPermissionSave", MODE_PRIVATE);

        mapPermission.setTag("map");
        storagePermission.setTag("storage");
        privacyPermission.setTag("privacy");
        marketingSnsPermission.setTag("marketing");

        setClickEvent();
        setContentClickEvent(mapPermission);
        setContentClickEvent(storagePermission);
        setContentClickEvent(privacyPermission);
    }
    @SuppressLint("ClickableViewAccessibility")
    private void setContentClickEvent(final CheckBox checkBox) {
        checkBox.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    if(event.getRawX() >= (checkBox.getRight() - checkBox.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        Intent intent = new Intent(GetPermissionBeforeRegister.this, Terms.class);

                        if(checkBox.getTag().equals("map")) {
                            intent.putExtra(Terms.SET_WEB_VIEW, Terms.LOCATION_INFORMATION);
                        }
                        if(checkBox.getTag().equals("storage")) {
                            intent.putExtra(Terms.SET_WEB_VIEW, Terms.TERMS_OF_SERVICE);
                        }
                        if(checkBox.getTag().equals("privacy")) {
                            intent.putExtra(Terms.SET_WEB_VIEW, Terms.PRIVACY_STATEMENT);
                        }
                        startActivity(intent);
                        CustomIntent.customType(GetPermissionBeforeRegister.this,"left-to-right");
                        return true;
                    }
                }
                return false;
            }
        });
    }
    private void setClickEvent() {
        allCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    mapPermission.setChecked(true);
                    storagePermission.setChecked(true);
                    privacyPermission.setChecked(true);
                    marketingSnsPermission.setChecked(true);
                    goNextBtn.setBackgroundColor(Color.rgb(131,51,230));
                }else {
                    mapPermission.setChecked(false);
                    storagePermission.setChecked(false);
                    privacyPermission.setChecked(false);
                    marketingSnsPermission.setChecked(false);
                    goNextBtn.setBackgroundColor(Color.rgb(171,171,171));
                }
            }
        });
    }
    public void check(View view) {
        if (mapPermission.isChecked() && storagePermission.isChecked() && privacyPermission.isChecked() ) {
            goNextBtn.setBackgroundColor(Color.rgb(131,51,230));
            if (marketingSnsPermission.isChecked()){
                marketingSnsPermissionSave.edit().putBoolean("marketingSnsPermissionSave", true).apply();
                allCheck.setChecked(true);
            }else{
                marketingSnsPermissionSave.edit().putBoolean("marketingSnsPermissionSave", false).apply();
            }
        }else{
            goNextBtn.setBackgroundColor(Color.rgb(171,171,171));
        }
    }

    public void onClickNext(View view) {
        if(mapPermission.isChecked() && storagePermission.isChecked() && privacyPermission.isChecked()) {
            startActivity(new Intent(GetPermissionBeforeRegister.this, Register1.class));
            finish();
        }
        else {
            Toast.makeText(this, "필수 사항을 체크해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
