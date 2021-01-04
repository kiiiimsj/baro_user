package com.example.wantchu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import maes.tech.intentanim.CustomIntent;

public class MapSettingPermission extends AppCompatActivity {
    CheckBox allCheck;
    CheckBox mapPermission;
    CheckBox storagePermission;
    CheckBox privacyPermission;
    CheckBox marketingSnsPermission;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permissison_before_register);
        allCheck = findViewById(R.id.all_check);
        mapPermission = findViewById(R.id.map_permission);
        storagePermission = findViewById(R.id.storage_permission);
        privacyPermission = findViewById(R.id.user_privacy_permission);
        marketingSnsPermission = findViewById(R.id.marketing_sns_permission);
        setClickEvent();
        setContentClickEvent(mapPermission);
        setContentClickEvent(storagePermission);
        setContentClickEvent(privacyPermission);
        setContentClickEvent(marketingSnsPermission);
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
                        startActivity(new Intent(MapSettingPermission.this, TermsOfUse.class));
                        CustomIntent.customType(MapSettingPermission.this,"left-to-right");
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
                }else {
                    mapPermission.setChecked(false);
                    storagePermission.setChecked(false);
                    privacyPermission.setChecked(false);
                    marketingSnsPermission.setChecked(false);
                }
            }
        });
    }

    public void onClickNext(View view) {
        if(mapPermission.isChecked() && storagePermission.isChecked() && privacyPermission.isChecked()) {
            startActivity(new Intent(MapSettingPermission.this, Register1.class));
        }
        else {
            Toast.makeText(this, "필수 사항을 체크해주세요", Toast.LENGTH_SHORT).show();
        }
    }
}
