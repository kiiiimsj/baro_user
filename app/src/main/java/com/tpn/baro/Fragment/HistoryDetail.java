package com.tpn.baro.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HistoryDetail extends Fragment {
    ImageButton deleteThis;
    RecyclerView recyclerView;
    TextView totals;
    TextView store;
    HistoryDetailAdapter historyDetailAdapter;
    Context context;
    public HistoryDetail(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View orderDetail = inflater.inflate(R.layout.fragment_history_detail, container,false);
        final Dialog dlg = new Dialog(context);
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dlg.setContentView(R.layout.fragment_history_detail);
        dlg.show();
        Bundle bundle = getArguments();
        String receipt_id = bundle.getString("receipt_id");
        String store_name = bundle.getString("storeName");
        String ordered_date = bundle.getString("orderedDate");
        int total_Price = bundle.getInt("totalPrice");
        orderDetail.setVisibility(View.VISIBLE);
        recyclerView = orderDetail.findViewById(R.id.historyDetailList);
        store = orderDetail.findViewById(R.id.store_name);
        totals = orderDetail.findViewById(R.id.totals);
        recyclerView.setLayoutManager(new LinearLayoutManager(orderDetail.getContext()));
        deleteThis = orderDetail.findViewById(R.id.deleteThis);
        deleteThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderDetail.setVisibility(View.INVISIBLE);
            }
        });
        totals.setText("총 결제 금액 : " + total_Price+"원");
        makeRequest(container.getContext(),receipt_id);
        store.setText(store_name);
        return orderDetail;
    }

    private synchronized void makeRequest(final Context context, String receipt_id) {
        String lastUrl = "OrderFindByReceiptId.do?receipt_id=" + receipt_id;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> historyDetailParsingHelpers
                                = jsonParsing(response);
                        applyAdapter(historyDetailParsingHelpers,context);
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
        return historyDetailParsing.getOrders();
    }
    private void applyAdapter(ArrayList<HistoryDetailParsing.HistoryDetailParsingHelper> historyDetailParsingHelpers,Context context){
        historyDetailAdapter = new HistoryDetailAdapter(historyDetailParsingHelpers,context);
        recyclerView.setAdapter(historyDetailAdapter);
    }
}
