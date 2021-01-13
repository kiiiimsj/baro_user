package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.example.wantchu.Basket;
import com.example.wantchu.MainPage;
import com.example.wantchu.OrderDetails;
import com.example.wantchu.R;

import static com.example.wantchu.Basket.BasketList;
import static com.example.wantchu.Basket.IN_MY_BASEKT;

public class StoreCloseDialog {

    private Context context;

    public StoreCloseDialog(Context context){
        this.context = context;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.store_close_dialog);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, MainPage.class);
                context.startActivity(intent);
                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;
                orderDetails.finish();
                dlg.dismiss();
            }
        });
    }
}
