package com.example.wantchu;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.OrderHistoryAdapter;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Fragment.HistoryDetail;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsing;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsingHelper;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {
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
//        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        if (sessionManager == null) {
            Log.i("Error update Session", sessionManager.toString());
        }
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        Log.i("height", metrics+"");
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


    private synchronized void makeRequest(final int state) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "OrderListFindByPhone.do?phone=" + phoneNumber + "&startPoint=" + currentPos;
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.i("OrderHistory", response);
                        orderHistoryParsing = jsonParsing(response, state);
                        applyAdapter(orderHistoryParsing);
                        orderHistoryAdapter.setOnItemClickListener(new OrderHistoryAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View v, int pos) {
                                Log.i("asdfa","qqqqqq");
                                Toast.makeText(getApplicationContext(),"asdfaag",Toast.LENGTH_LONG).show();
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
        }
        Gson gson = new Gson();
        OrderHistoryParsing addOrder = gson.fromJson(result, OrderHistoryParsing.class);
        if(!addOrder.getResult()) {
            Toast.makeText(this, "더 불러올 내역이 없습니다.", Toast.LENGTH_SHORT).show();
            return orderHistoryParsing;
        }
        else {
            //Collections.sort(addOrder.order);
            orderHistoryParsing.order.addAll(addOrder.order);
            return orderHistoryParsing;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}