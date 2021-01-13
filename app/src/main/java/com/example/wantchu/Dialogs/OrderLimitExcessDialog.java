package com.example.wantchu.Dialogs;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.wantchu.R;

public class OrderLimitExcessDialog {
    public Context context;
    public String errorMessage;
    public OrderLimitExcessDialog(Context context, String errorMessage){
        this.context = context;
        this.errorMessage = errorMessage;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_order_limit_excess);
        TextView errorMessageView = dlg.findViewById(R.id.error_message);
        errorMessageView.setText(errorMessage);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}