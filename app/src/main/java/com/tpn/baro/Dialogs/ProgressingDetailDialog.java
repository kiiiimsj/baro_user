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
    ImageButton delete_this;
    Button okay;
    RecyclerView progressDetail;
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
        gson = new Gson();
        OrderProgressingParsingHelper data = gson.fromJson(json,OrderProgressingParsingHelper.class);

        //////////////////////////////
        store_name = progressingDetail.findViewById(R.id.store_name);
        totals = progressingDetail.findViewById(R.id.totals);
        request = progressingDetail.findViewById(R.id.request);
        delete_this = progressingDetail.findViewById(R.id.deleteThis);
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
        totals.setText("총 결제 금액 : " + data.getTotal_price()+"");
        progressDetail.setLayoutManager(new LinearLayoutManager(context));


        /////////////////////////////
        builder.setView(progressingDetail);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        makeRequest(data.getReceipt_id());
        return dialog;
    }

    private void makeRequest(final String receipt_id) {
        String url = new UrlMaker().UrlMake("OrderProgressingDetail.do?receipt_id=") + receipt_id;
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsing(response);
                        applyAdapter(orderProgressDetailParsing.getOrders());
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

    private void applyAdapter(ArrayList<OrderProgressDetailParsing.OrderProgressDetailParsingHelper> orders) {
        ProgressDetailAdapter adapter = new ProgressDetailAdapter(orders,context);
        progressDetail.setAdapter(adapter);
    }

    private void jsonParsing(String response) {
        orderProgressDetailParsing = gson.fromJson(response,OrderProgressDetailParsing.class);

    }

    private void dismissDialog() {
        this.dismiss();
    }
}
