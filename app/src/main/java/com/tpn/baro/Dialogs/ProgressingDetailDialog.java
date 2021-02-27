package com.tpn.baro.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.ProgressDetailAdapter;
import com.tpn.baro.JsonParsingHelper.OrderProgressDetailParsing;
import com.tpn.baro.JsonParsingHelper.OrderProgressingParsingHelper;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProgressingDetailDialog extends DialogFragment {
    private Context context;

    TextView store_name;
    TextView totals;
    TextView request;
    TextView discountPrice;
//    TextView discountRatePrice;
    ImageButton delete_this;
    Button okay;
    RecyclerView progressDetail;
    OrderProgressingParsingHelper data;
    Gson gson;

    OrderProgressDetailParsing orderProgressDetailParsing;
    public static ProgressingDetailDialog newInstance(Context context){
        Bundle bundle = new Bundle();
        ProgressingDetailDialog fragment = new ProgressingDetailDialog();
        fragment.context = context;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View progressingDetail = getActivity().getLayoutInflater().inflate(R.layout.fragment_progressing_detail, null);

        String json = getArguments().getString("json");
        Log.e("json" , json);
        gson = new Gson();
        data = gson.fromJson(json,OrderProgressingParsingHelper.class);

        makeRequest(data.getReceipt_id(), data.getDiscount_rate());
        //////////////////////////////
        store_name = progressingDetail.findViewById(R.id.store_name);
        totals = progressingDetail.findViewById(R.id.totals);
        request = progressingDetail.findViewById(R.id.request);
        delete_this = progressingDetail.findViewById(R.id.deleteThis);
        discountPrice = progressingDetail.findViewById(R.id.discount_price);
//        discountRatePrice = progressingDetail.findViewById(R.id.discount_rate_price);
        progressDetail = progressingDetail.findViewById(R.id.ProgressDetailList);
        okay = progressingDetail.findViewById(R.id.okay);


        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });

        delete_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        store_name.setText(data.getStore_name());
        progressDetail.setLayoutManager(new LinearLayoutManager(context));
        if(data.getCoupon_discount() == 0 ) {
            discountPrice.setVisibility(View.GONE);
        }
//        if(data.getDiscount_rate() == 0 ) {
//            discountRatePrice.setVisibility(View.GONE);
//        }

//        discountRatePrice.setText("바로 할인 금액 : "+ (int)(data.getTotal_price() * (data.getDiscount_rate() / 100.0))+"원");
        totals.setText("총 결제 금액 : " + (data.getTotal_price() - (int)(data.getTotal_price() * (data.getDiscount_rate() / 100.0)) - data.getCoupon_discount())+"원");
        /////////////////////////////
        builder.setView(progressingDetail);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);


        return dialog;
    }

    private void makeRequest(final String receipt_id, final int discountRate) {
        String url = new UrlMaker().UrlMake("OrderProgressingDetail.do?receipt_id=") + receipt_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        jsonParsing(response);
                        applyAdapter(orderProgressDetailParsing, discountRate);
                        if (orderProgressDetailParsing.getRequests().equals("")) {
                            request.setText("요청사항이 없었습니다.");
                        } else {
                            request.setText(orderProgressDetailParsing.getRequests());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("success",error.toString());

                    }
                });
        requestQueue.add(stringRequest);
    }

    private void applyAdapter(OrderProgressDetailParsing orders, int discountRate) {
        ProgressDetailAdapter adapter = new ProgressDetailAdapter(orders,context);
        progressDetail.setAdapter(adapter);
//        int dicounted_price = 0;
//        for (int i = 0; i < orders.size() ; i++) {
//            OrderProgressDetailParsing.OrderProgressDetailParsingHelper order = orders.get(i);
//            dicounted_price += order.getMenu_defaultprice();
//        }
        if(orderProgressDetailParsing.getDiscount_rate() == 0 && orderProgressDetailParsing.getCoupon_discount() == 0) {
            discountPrice.setVisibility(View.GONE);
        }

        discountPrice.setText("쿠폰 할인 금액 : " + orderProgressDetailParsing.getCoupon_discount() +"원");

    }

    private void jsonParsing(String response) {
        orderProgressDetailParsing = gson.fromJson(response,OrderProgressDetailParsing.class);

    }

    private void dismissDialog() {
        this.dismiss();
    }
}
