package com.example.wantchu.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
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
import com.example.wantchu.Adapter.ProgressDetailAdapter;
import com.example.wantchu.JsonParsingHelper.OrderProgressDetailParsing;
import com.example.wantchu.JsonParsingHelper.OrderProgressingParsingHelper;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ProgressingDetailDialog extends DialogFragment {
    private Context context;

    TextView store_name;
    TextView totals;

    ImageButton delete_this;
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
    public void onResume() {
        super.onResume();
        final int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 400, getResources().getDisplayMetrics());

        final int height  = ActionBar.LayoutParams.WRAP_CONTENT;


        getDialog().getWindow().setLayout(width, height);
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

        delete_this = progressingDetail.findViewById(R.id.deleteThis);
        progressDetail = progressingDetail.findViewById(R.id.ProgressDetailList);

        delete_this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissDialog();
            }
        });
        store_name.setText(data.getStore_name());
        totals.setText(data.getTotal_price()+"");
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
                        Log.e("success",response);
                        jsonParsing(response);
                        applyAdapter(orderProgressDetailParsing.getOrders());
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
