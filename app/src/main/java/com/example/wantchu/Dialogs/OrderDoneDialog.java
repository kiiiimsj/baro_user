package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.wantchu.Basket;
import com.example.wantchu.MainPage;
import com.example.wantchu.R;

public class OrderDoneDialog {

    private Context context;

    public OrderDoneDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_order_done);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainPage.class);
                context.startActivity(intent);
                Basket basket = (Basket) Basket.basket;
                basket.finish();
                dlg.dismiss();
            }
        });
    }
}
