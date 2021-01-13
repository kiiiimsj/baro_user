package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tpn.baro.R;

import maes.tech.intentanim.CustomIntent;

public class ChangePass3 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass3);
    }

    public void goToLogin(View view) {
        startActivity(new Intent(ChangePass3.this,Login.class));
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }
}