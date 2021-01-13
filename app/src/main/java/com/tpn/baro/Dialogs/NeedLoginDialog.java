package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.tpn.baro.Login;
import com.tpn.baro.R;

public class NeedLoginDialog {
    private Context context;

    public NeedLoginDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.setContentView(R.layout.dialog_need_login);
        dlg.show();

        Window window = dlg.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.dialog_invisible)));

        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

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
        ConstraintLayout outSide = dlg.findViewById(R.id.outSide);
        outSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
