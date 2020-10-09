package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(FirstPage.this, Login.class);
                startActivity(intent);
                finish();
            }
        },SLIDE_TIMER);
    }
}