package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.tpn.baro.JsonParsingHelper.DefaultParsing;
import com.tpn.baro.R;

public class CouponRegisterDialog {
    private Context context;
    DefaultParsing defaultParsing;
    public CouponRegisterDialog(Context context, DefaultParsing defaultParsing){
        this.defaultParsing = defaultParsing;
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_add_favorite);
        TextView title = (TextView) dlg.findViewById(R.id.title);
        TextView message = (TextView) dlg.findViewById(R.id.mesgase);
        if (defaultParsing.getResult()) {
            title.setText("등록 성공");
        } else {
            title.setText("등록 실패");
//            message.setText("이미 사용하신 쿠폰번호이거나 \n 존재하지않는 쿠폰번호입니다.");
        }
        message.setText(defaultParsing.getMessage());
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
