package com.tpn.baro;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.OrderHistoryAdapter;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.JsonParsingHelper.OrderHistoryParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {
    final private String TAG = this.getClass().getSimpleName();
    private final static int FIRST = 1;
    private final static int AFTER_FIRST = 2;
    String phoneNumber;
    int currentPos = 0;
    SwipyRefreshLayout refreshLayout;
    SessionManager sessionManager;
    OrderHistoryParsing orderHistoryParsing;
    RecyclerView recyclerView;
    OrderHistoryAdapter orderHistoryAdapter;

    ProgressApplication progressApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        refreshLayout = findViewById(R.id.refresh_list);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String,String> hashMap = sessionManager.getUsersDetailFromSession();
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        refreshLayout.setDistanceToTriggerSync((metrics.heightPixels /4) + 50);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                currentPos+=20;
                makeRequest(AFTER_FIRST);
            }
        });
        phoneNumber = hashMap.get(SessionManager.KEY_PHONENUMBER);
        recyclerView = findViewById(R.id.orderHistorylist);
        makeRequest(FIRST);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(0, 0);
    }
    private synchronized void makeRequest(final int state) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "OrderListFindByPhone.do?phone=" + phoneNumber + "&startPoint=" + currentPos;
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        orderHistoryParsing = jsonParsing(response, state);
                        applyAdapter(orderHistoryParsing);
                        orderHistoryAdapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Toast.makeText(OrderHistory.this,"asdfaag",Toast.LENGTH_LONG).show();
                            }
                        });
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

    private void applyAdapter(OrderHistoryParsing order) {
        orderHistoryAdapter = new OrderHistoryAdapter(order.getOrder(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(orderHistoryAdapter);
        progressApplication.progressOFF();
        refreshLayout.setRefreshing(false);
    }

    private OrderHistoryParsing jsonParsing(String result, int state) {
        if(state == FIRST) {
            Gson gson = new Gson();
            orderHistoryParsing = gson.fromJson(result, OrderHistoryParsing.class);
            //Collections.sort(orderHistoryParsing.order);
            return  orderHistoryParsing;
        }else {
            Gson gson = new Gson();
            OrderHistoryParsing addOrder = gson.fromJson(result, OrderHistoryParsing.class);
            if (!addOrder.getResult()) {
                Toast.makeText(this, "더 불러올 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                return orderHistoryParsing;
            } else {
                //Collections.sort(addOrder.order);
                orderHistoryParsing.order.addAll(addOrder.order);
                return orderHistoryParsing;
            }
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainPage.class));
        finish();
    }
}