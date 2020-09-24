package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.wantchu.Basket;
import com.example.wantchu.OrderDetails;
import com.example.wantchu.R;

public class CustomDialog {

    private Context context;
    private int storeId;
    public CustomDialog(Context context, int storeId) {
        this.context = context;
        this.storeId = storeId;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.custom_dialog);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        final Button goBasket = (Button) dlg.findViewById(R.id.goBasket);
        final Button goStore = (Button) dlg.findViewById(R.id.goStore);

        goBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "\" 을 입력하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, Basket.class);
                intent.putExtra("onDialog", true);
                context.startActivity(intent);
                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;
                orderDetails.finish();
                dlg.dismiss();
            }
        });
        goStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;
                orderDetails.finish();
                dlg.dismiss();
            }
        });
    }

}