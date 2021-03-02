package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tpn.baro.R;

public class OrderLimitExcessDialog {
    public Context context;
    public String errorMessage;
    public interface OnDismiss{
        void clickErrorDialogClick();
    }
    public OnDismiss onDismiss;
    public OrderLimitExcessDialog(Context context, String errorMessage, OnDismiss onDismiss){
        this.onDismiss = onDismiss;
        this.context = context;
        this.errorMessage = errorMessage;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_order_limit_excess);
        dlg.setCancelable(false);
        TextView errorMessageView = dlg.findViewById(R.id.error_message);
        errorMessageView.setText(errorMessage);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                onDismiss.clickErrorDialogClick();
            }
        });
    }
}