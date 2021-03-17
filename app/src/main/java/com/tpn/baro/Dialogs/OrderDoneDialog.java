package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tpn.baro.Basket;
import com.tpn.baro.NewMainPage;
import com.tpn.baro.R;
import com.tpn.baro.StoreInfoReNewer;

public class OrderDoneDialog {

    private Context context;

    public OrderDoneDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_order_done);
        dlg.setCancelable(false);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, NewMainPage.class);
                context.startActivity(intent);
                dlg.dismiss();
                Basket basket = (Basket) Basket.basket;
                basket.finish();


            }
        });
    }
}
