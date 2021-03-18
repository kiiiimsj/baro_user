package com.tpn.baro.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.AvailableCouponsAdapter;
import com.tpn.baro.JsonParsingHelper.AvailableCouponsParsing;
import com.tpn.baro.JsonParsingHelper.AvailableCouponsParsingHelper;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class CouponDialog extends DialogFragment {
    private Context context;
    private RecyclerView recyclerView;
    private TextView doNotHave;
    private CouponDialogListener couponDialogListener;
    TextView realTotal;
    TextView totalPriceText;
    TextView discountMoney;
    TextView request;
    Button pay;
    EditTextfocusListener editTextfocusListener;
    View coupons;
    static CouponDialog couponDialog;
    public static CouponDialog newInstance(Context context,CouponDialogListener couponDialogListener,EditTextfocusListener editTextfocusListener){
        Bundle bundle = new Bundle();
        couponDialog = new CouponDialog();
        couponDialog.setArguments(bundle);
        couponDialog.context = context;
        couponDialog.couponDialogListener = couponDialogListener;
        couponDialog.editTextfocusListener = editTextfocusListener;
        return couponDialog;
    }
    public interface CouponDialogListener{
        void clickBtn(int discountTotal,int dc,int coupon_id,String orderRequest);
    }
    public interface EditTextfocusListener{
        void isFocus(Boolean hasFocus);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        coupons = getActivity().getLayoutInflater().inflate(R.layout.fragment_dialog_coupon,null);
        Bundle bundle = getArguments();

        ////////////////////////////////////////////////////////////////////////
        recyclerView = coupons.findViewById(R.id.coupon_List_Shell);
        doNotHave = coupons.findViewById(R.id.doNotHave);
        realTotal = coupons.findViewById(R.id.realTotal);
        totalPriceText = coupons.findViewById(R.id.total);
        discountMoney = coupons.findViewById(R.id.discount_money);
        pay =coupons.findViewById(R.id.pay);
        request = coupons.findViewById(R.id.request);
        ImageButton close = coupons.findViewById(R.id.close_coupon);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        /////////////////////////////////////////////////////////////////////////



        /////////////////////////////////////////////////////////////////////////
        String phone = bundle.getString("phone");
        int discountAmount = Integer.parseInt(discountMoney.getText().toString());
        int totalPrice = bundle.getInt("totalPrice");
        totalPriceText.setText(totalPrice+"");
        realTotal.setText((totalPrice-discountAmount) <=0 ? ""+0 : ""+(totalPrice-discountAmount) );
        makeRequest(phone,totalPrice);
        /////////////////////////////////////////////////////////////////////////
        builder.setView(coupons);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }
    private void dismissDialog() {
        this.dismiss();
    }

    private void makeRequest(String phone,int totalPrice){
        RequestQueue requestQueue  = Volley.newRequestQueue(context);
        String lastUrl = "CouponFindUsable.do?phone=" + phone + "&price=" + totalPrice;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        AvailableCouponsParsing availableCouponsParsing = jsonParsing(response);
                        applyAdapter(availableCouponsParsing);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("fail",error.toString());
                    }
                });
        requestQueue.add(stringRequest);
    }

    private void applyAdapter(AvailableCouponsParsing availableCouponsParsing) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        if(!availableCouponsParsing.getResult()){
            doNotHave.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            doNotHave.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
        ArrayList<AvailableCouponsParsingHelper> availableCouponsParsingHelpers = availableCouponsParsing.getCoupon();
        final AvailableCouponsAdapter availableCouponsAdapter = new AvailableCouponsAdapter(availableCouponsParsingHelpers,realTotal,totalPriceText,discountMoney);
        recyclerView.setAdapter(availableCouponsAdapter);
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(availableCouponsAdapter.getItemCount()!=0) {
                    couponDialogListener.clickBtn(availableCouponsAdapter.getDiscountTotal(), availableCouponsAdapter.getDc(),
                            availableCouponsAdapter.getCoupon_id(),request.getText().toString());
                }
                else{
                    couponDialogListener.clickBtn(Integer.parseInt(totalPriceText.getText().toString()),-1,-1,request.getText().toString());
                }
                dismissDialog();
            }
        });
        request.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (focus){
                    WindowManager.LayoutParams lp = couponDialog.getActivity().getWindow().getAttributes();
                    lp.y = Gravity.TOP - 80;
                    couponDialog.getActivity().getWindow().setAttributes(lp);
                }else{
                    WindowManager.LayoutParams lp = couponDialog.getActivity().getWindow().getAttributes();
                    lp.y = Gravity.CENTER;
                    couponDialog.getActivity().getWindow().setAttributes(lp);
                }
            }
        });
    }

    private AvailableCouponsParsing jsonParsing(String response) {
        Gson gson = new Gson();
        AvailableCouponsParsing availableCouponsParsing = gson.fromJson(response,AvailableCouponsParsing.class);
        return availableCouponsParsing;
    }
}