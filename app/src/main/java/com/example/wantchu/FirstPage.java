package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.pedro.library.AutoPermissions;

public class FirstPage extends AppCompatActivity {
    TextView orderText;
    TextView getText;
    TextView baroLogo;

    Animation sideLeftAnim, bottomAnim, rotate_0_to_20, rotate_20_to_0, rotate_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_page);

        SharedPreferences shf = this.getSharedPreferences("basketList", MODE_PRIVATE);
        SharedPreferences.Editor editor = shf.edit();
        editor.clear().commit();

        orderText = findViewById(R.id.order_text);
        getText = findViewById(R.id.get_text);
        baroLogo =findViewById(R.id.baro_logo);

        sideLeftAnim = AnimationUtils.loadAnimation(this, R.anim.anim_slide_in_left);
        rotate_0_to_20 = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_0_to_20);
        rotate_20_to_0 = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_20_to_0);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);
        rotate_0 = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_0);

        getText.setVisibility(View.INVISIBLE);
        baroLogo.setVisibility(View.INVISIBLE);
        orderText.setAnimation(sideLeftAnim);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getText.setVisibility(View.VISIBLE);
                getText.setAnimation(rotate_0_to_20);

            }
        }, 800);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                baroLogo.setVisibility(View.VISIBLE);
                baroLogo.startAnimation(bottomAnim);
                getText.startAnimation(rotate_20_to_0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(FirstPage.this, MainPage.class);
                        startActivity(intent);
                        finish();
                    }
                }, 800);
            }
        }, 800);
    }
}