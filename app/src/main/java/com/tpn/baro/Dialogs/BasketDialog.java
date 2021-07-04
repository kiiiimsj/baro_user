package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tpn.baro.Basket;
import com.tpn.baro.OrderDetails;
import com.tpn.baro.R;

import maes.tech.intentanim.CustomIntent;

public class BasketDialog {

    private Context context;
    private int storeId;
    private int discountRate;
    public BasketDialog(Context context, int storeId, int discountRate) {
        this.context = context;
        this.storeId = storeId;
        this.discountRate = discountRate;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_basket_add_menu);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        final Button goBasket = (Button) dlg.findViewById(R.id.goBasket);
        final Button goStore = (Button) dlg.findViewById(R.id.goStore);

        goBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Basket.class);
                intent.putExtra("onDialog", true);
                intent.putExtra("discount_rate", discountRate);
                context.startActivity(intent);

                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;

                orderDetails.finish();
                CustomIntent.customType(context,"left-to-right");
                dlg.dismiss();
            }
        });
        goStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;
                orderDetails.finish();
                dlg.dismiss();
            }
        });
    }

}