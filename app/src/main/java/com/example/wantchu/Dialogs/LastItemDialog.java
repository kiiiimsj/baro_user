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

public class LastItemDialog  {
    private Context context;
    private DoDelete mListener;

    public LastItemDialog(Context context,DoDelete mListener) {
        this.context = context;
        this.mListener = mListener;
    }

        public interface DoDelete {
        void doDelete();
    }
    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.last_item_dialog);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        final Button delete = (Button) dlg.findViewById(R.id.delete);
        final Button doNotDelete = (Button) dlg.findViewById(R.id.doNotDelete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Basket basket = (Basket) Basket.basket;
//                basket.finish();
                mListener.doDelete();
                dlg.dismiss();
            }
        });
        doNotDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "취소 했습니다.", Toast.LENGTH_SHORT).show();
                dlg.dismiss();
            }
        });
    }
}
