package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tpn.baro.R;

public class IfLogoutDialog {
    public Context context;
    public interface clickButton {
        void clickOkay();
        void clickCancel();
    }
    public clickButton mListener;
    public IfLogoutDialog(Context context, clickButton mListener){
        this.context = context;
        this.mListener = mListener;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_if_logout);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clickOkay();
                dlg.dismiss();
            }
        });
        final Button cancel = (Button) dlg.findViewById(R.id.dialog_close);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.clickCancel();
                dlg.dismiss();
            }
        });
    }
}
