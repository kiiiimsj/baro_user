package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tpn.baro.JsonParsingHelper.DefaultParsing;
import com.tpn.baro.R;

public class BootPayFiveMinDialog {
    public static final int BOOT_PAY_ACTION = 0;
    public static final int OVER_5 = 1;
    public static final int CLOSE_BEFORE = 2;
    public static final int FIVE_MIN = 20;

    private Context context;
    public String outSideMessage;
    public String outSideTitle;
    public interface OnDismiss{
        void clickDismiss();
    }

    public BootPayFiveMinDialog.OnDismiss onDismiss;
    public BootPayFiveMinDialog(BootPayFiveMinDialog.OnDismiss onDismiss, Context context){
        this.onDismiss = onDismiss;
        this.context = context;
    }
    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setCancelable(false);
        dlg.setContentView(R.layout.fragment_add_favorite);
        TextView title = (TextView) dlg.findViewById(R.id.title);
        TextView message = (TextView) dlg.findViewById(R.id.mesgase);
        if(outSideTitle == null) {
            title.setText("페이지 만료");
        }else {
            title.setText(outSideTitle);
        }
        message.setText(outSideMessage);

        dlg.show();
        final Button okay = (Button) dlg.findViewById(R.id.okay);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismiss.clickDismiss();
                dlg.dismiss();
            }
        });
    }
}
