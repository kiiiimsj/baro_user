package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tpn.baro.R;

public class BootPayDialog {
    public static final int ON_ERROR = 0;
    public static final int ON_CANCEL = 1;
    public static final int ON_DONE = 2;

    public String content;
    public int getBootPayType;
    TextView contentTextView;
    TextView title;
    public interface OnDismiss{
        void clickDismiss();
    }
    private Context context;

    public OnDismiss onDismiss;
    public BootPayDialog(Context context, OnDismiss onDismiss, String content, int getBootPayType){
        this.onDismiss = onDismiss;
        this.context = context;
        this.content = content;
        this.getBootPayType = getBootPayType;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_order_cancel);
        dlg.setCancelable(false);
        contentTextView = dlg.findViewById(R.id.content);
        title = dlg.findViewById(R.id.title);
        
        setContent();
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
                onDismiss.clickDismiss();
            }
        });
    }
    public void setContent() {
        switch (getBootPayType) {
            case ON_ERROR:
                if(content.equals("error:-15002")) {
                    contentTextView.setText("주문 결제가 취소 되었습니다.");
                }else {
                    contentTextView.setText(content);
                }
                break;
            case ON_DONE:
                title.setText("주문 완료");
                contentTextView.setText("주문 결제가 완료 되었습니다.");
                break;
            case ON_CANCEL:
            default:
                contentTextView.setText("주문 결제가 취소 되었습니다.");
                break;
        }
    }
}
