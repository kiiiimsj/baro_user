package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wantchu.MainPage;
import com.example.wantchu.OrderDetails;
import com.example.wantchu.R;

public class MapSetPositionDialog {
    private Context context;
    TextView dialogMessage;
    boolean clickOkay = false;
    isClickOkay mListener;
    public MapSetPositionDialog(Context context, isClickOkay mListener) {
        this.context = context;
        this.mListener = mListener;
    }
    public interface isClickOkay {
        void clickOkay();
    }
    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.custom_dialog);
        dialogMessage = dlg.findViewById(R.id.mesgase);
        dialogMessage.setText("해당 위치를 현재위치로 설정하시겠습니까?");
        dlg.show();

        final Button yes = (Button) dlg.findViewById(R.id.goBasket);
        yes.setText("설정");
        final Button no = (Button) dlg.findViewById(R.id.goStore);
        no.setText("취소");

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clickOkay();
                dlg.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
}
