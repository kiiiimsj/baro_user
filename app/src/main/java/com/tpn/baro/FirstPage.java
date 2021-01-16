package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class FirstPage extends AppCompatActivity {
    TextView orderText;
    TextView getText;
    TextView baroLogo;

    Animation sideLeftAnim, bottomAnim, rotated_35, rotated_35_to_0;

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
        rotated_35 = AnimationUtils.loadAnimation(this, R.anim.anim_rotated_35);
        rotated_35_to_0 = AnimationUtils.loadAnimation(this, R.anim.anim_rotated_35_to_0);
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_anim);

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
                                Intent intent = new Intent(FirstPage.this, MainPage.class);
                                startActivity(intent);
                                finish();
                            }
                        }, 800);
                    }
                }, 800);
            }
        }, 800);
    }
}