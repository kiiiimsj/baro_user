package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tpn.baro.R;

public class LastItemDialog  {
    private Context context;
    private DoDelete mListener;
    private Boolean isDeleteAll;

    public LastItemDialog(Context context,DoDelete mListener,Boolean isDeleteAll) {
        this.context = context;
        this.mListener = mListener;
        this.isDeleteAll = isDeleteAll;
    }

        public interface DoDelete {
        void doDelete();
    }
    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.dialog_last_item);
        dlg.setCancelable(false);
        dlg.setCanceledOnTouchOutside(false);


        final Button delete = (Button) dlg.findViewById(R.id.delete);
        final Button doNotDelete = (Button) dlg.findViewById(R.id.doNotDelete);

        TextView title = dlg.findViewById(R.id.title);
        TextView message = dlg.findViewById(R.id.content);

        if (isDeleteAll){
            title.setText("모두 비우기");
            message.setText("장바구니의 물품을 모두 비우시겠습니까?");
        }else{

        }
        dlg.show();

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
