package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.tpn.baro.Basket;
import com.tpn.baro.OrderDetails;
import com.tpn.baro.R;

import static com.tpn.baro.Basket.BasketList;

public class RefreshBasketByStoreIdDialog {

    private Context context;
    private String store_id;
    private RefreshBasketByStoreIdDialogListener refreshBasketByStoreIdDialogListener;

    public interface RefreshBasketByStoreIdDialogListener{
        void clickBtn(Boolean OkOrNo);
    }

    public RefreshBasketByStoreIdDialog(Context context,String store_id,RefreshBasketByStoreIdDialogListener refreshBasketByStoreIdDialogListener){
        this.context = context;
        this.store_id = store_id;
        this.refreshBasketByStoreIdDialogListener = refreshBasketByStoreIdDialogListener;
    }

    public void callFunction() {
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.refresh_basket_by_store_id);
        dlg.setCancelable(false);
        dlg.show();

        final Button okay = (Button) dlg.findViewById(R.id.okay);
        final Button no = (Button) dlg.findViewById(R.id.no);
        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(BasketList,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Basket.IN_MY_BASEKT,"");
                editor.commit();
                refreshBasketByStoreIdDialogListener.clickBtn(true);
                dlg.dismiss();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshBasketByStoreIdDialogListener.clickBtn(false);
                OrderDetails orderDetails = (OrderDetails)OrderDetails.orderDetails;
                orderDetails.finish();
                dlg.dismiss();
            }
        });
    }
}
