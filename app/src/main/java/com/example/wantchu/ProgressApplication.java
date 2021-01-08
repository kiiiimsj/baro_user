package com.example.wantchu;

import android.app.Activity;
import android.app.Application;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

public class ProgressApplication extends Application {

    private static ProgressApplication progressApplication;
    AppCompatDialog progressDialog;

    public static ProgressApplication getInstance(){
        return progressApplication;
    }

    @Override
    public void onCreate(){
        super.onCreate();

        progressApplication = this;
    }

    public void progressON(Activity activity){
        if(activity == null || activity.isFinishing()){
            return;
        }

        if(progressDialog != null && progressDialog.isShowing()){
            progressSET();
        }
        else{
            progressDialog = new AppCompatDialog(activity);
            progressDialog.setCancelable(false);
            progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            progressDialog.setContentView(R.layout.progress_loading);
            progressDialog.show();
        }

        final ProgressBar img_loading_frame = (ProgressBar) progressDialog.findViewById(R.id.frame_loading);
        img_loading_frame.setVisibility(View.VISIBLE);

    }

    public void progressSET() {

        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
    }
    public void progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            //img_loading_frame.setVisibility(View.GONE);
            progressDialog.dismiss();
        }
    }
}
