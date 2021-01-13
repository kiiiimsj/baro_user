package com.tpn.baro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.tpn.baro.R;

import maes.tech.intentanim.CustomIntent;

public class ChangeEmail2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email2);
    }

    public void goToMyPage(View view) {
        startActivity(new Intent(ChangeEmail2.this, MyPage.class));
        finish();
    }
    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this,"right-to-left");
    }

}