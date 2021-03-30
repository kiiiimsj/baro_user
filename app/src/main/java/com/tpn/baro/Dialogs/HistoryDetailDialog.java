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
import com.tpn.baro.Adapter.HistoryDetailAdapter;
import com.tpn.baro.JsonParsingHelper.HistoryDetailParsing;
import com.tpn.baro.JsonParsingHelper.OrderProgressDetailParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HistoryDetailDialog extends DialogFragment {
    private Context context;
    ImageButton deleteThis;
    RecyclerView recyclerView;
    TextView totals;
    TextView store;
    TextView requests;
    TextView discountPrice;
//    TextView discountRatePrice;
    HistoryDetailAdapter historyDetailAdapter;
    Button close_btn;
    int discountRate = 0;
    int coupon_discount = 0;
    int total_Price = 0;
    public static HistoryDetailDialog newInstance(Context context) {
        Bundle bundle = new Bundle();
        HistoryDetailDialog fragment = new HistoryDetailDialog();
        fragment.context = context;
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View orderDetail = getActivity().getLayoutInflater().inflate(R.layout.fragment_history_detail, null);
        String receipt_id = getArguments().getString("receipt_id");
        String store_name = getArguments().getString("storeName");
        String ordered_date = getArguments().getString("orderedDate");
        discountRate = getArguments().getInt("discount_rate");
        total_Price = getArguments().getInt("totalPrice");
        coupon_discount = getArguments().getInt("coupon_discount");

        makeRequest(context,receipt_id);

        orderDetail.setVisibility(View.VISIBLE);
        close_btn = orderDetail.findViewById(R.id.close_dialog);
        recyclerView = orderDetail.findViewById(R.id.historyDetailList);
        requests = orderDetail.findViewById(R.id.request);
        store = orderDetail.findViewById(R.id.store_name);
        totals = orderDetail.findViewById(R.id.totals);
        discountPrice = orderDetail.findViewById(R.id.discount_price);
//        discountRatePrice = orderDetail.findViewById(R.id.discount_rate_price);
        recyclerView.setLayoutManager(new LinearLayoutManager(orderDetail.getContext()));
        deleteThis = orderDetail.findViewById(R.id.deleteThis);
        deleteThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDetail.setVisibility(View.INVISIBLE);
                dismissDialog();
            }
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        Log.e("coupon_discount", coupon_discount+"");
        if(coupon_discount == 0) {
            discountPrice.setVisibility(View.GONE);
        }
//        if(discountRate == 0) {
//            discountRatePrice.setVisibility(View.GONE);
//        }
//        discountRatePrice.setText("바로 할인 금액 : "+(int)(total_Price * (discountRate / 100.0))+ "원");
        totals.setText("총 결제 금액 : " + (total_Price -(int)(total_Price * (discountRate / 100.0)) - coupon_discount) +"원");

        store.setText(store_name);
        builder.setView(orderDetail);
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
    private synchronized void makeRequest(final Context context, String receipt_id) {
        String lastUrl = "OrderFindByReceiptId.do?receipt_id=" +receipt_id;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.e("response", response);
                        ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> historyDetailParsingHelpers
                                = jsonParsing(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderHistory", "error");
                    }
                });
        requestQueue.add(request);
    }

    private ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> jsonParsing(String result){
        Gson gson = new Gson();
        HistoryDetailParsing historyDetailParsing = gson.fromJson(result,HistoryDetailParsing.class);
        if (historyDetailParsing.getRequests().equals("")) {
            requests.setText("요청사항 없음.");
        } else {
            applyAdapter(historyDetailParsing,context);
            requests.setText(historyDetailParsing.getRequests());
        }
        return historyDetailParsing.getOrders();
    }
    private void applyAdapter(HistoryDetailParsing historyDetailParsingHelpers,Context context){
        historyDetailAdapter = new HistoryDetailAdapter(historyDetailParsingHelpers,context);
        recyclerView.setAdapter(historyDetailAdapter);

//        int dicounted_price = 0;
//        for (int i = 0; i < historyDetailParsingHelpers.size() ; i++) {
//            HistoryDetailParsing.HistoryDetailParsingHelper historyDetailParsingHelper = historyDetailParsingHelpers.get(i);
//            dicounted_price += historyDetailParsingHelper.getMenu_defaultprice();
//        }
//
//        int productOriginPrice = ((dicounted_price * 100) / (100 - discountRate));

        discountPrice.setText("쿠폰 할인 금액 : " + coupon_discount +"원");
    }

    public Bundle getBundle() {
        return this.getArguments();
    }
    private void dismissDialog() {
        this.dismiss();
    }



}
