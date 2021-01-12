package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.HistoryDetailAdapter;
import com.example.wantchu.JsonParsingHelper.HistoryDetailParsing;
import com.example.wantchu.Login;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NeedLoginDialog {
    private Context context;

    public NeedLoginDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context, R.style.mydialog);
        dlg.setContentView(R.layout.dialog_need_login);
        dlg.show();

        Window window = dlg.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.dialog_invisible)));

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ConstraintLayout backgroud = dlg.findViewById(R.id.background);

        ImageButton dismissBtn = (ImageButton) dlg.findViewById(R.id.dismiss);
        dismissBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });

        Button loginBtn = (Button) dlg.findViewById(R.id.goLogin);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, Login.class));
                dlg.dismiss();
//                context.overridePendingTransition(0, 0);
            }
        });
        backgroud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}
