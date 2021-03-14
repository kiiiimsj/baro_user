package com.tpn.baro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
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
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.ProgressApplication;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class OrderHistory extends AppCompatActivity {
    final private String TAG = this.getClass().getSimpleName();
    private final static int FIRST = 1;
    private final static int AFTER_FIRST = 2;
    private String phoneNumber;
    private int currentPos = 0;
    private NestedScrollView refreshLayout;
    private ProgressBar refreshCircle;
    private boolean isLoading = false;
    private boolean noMoreData = false;

    SessionManager sessionManager;
    OrderHistoryParsing orderHistoryParsing;

    RecyclerView recyclerView;
    OrderHistoryAdapter orderHistoryAdapter;

    ProgressApplication progressApplication;
    private boolean fromMyPage = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(OrderHistory.this, this.toString());
        }
        setContentView(R.layout.activity_order_history);
        fromMyPage = getIntent().getBooleanExtra("from_my_page", false);

        refreshLayout = findViewById(R.id.refreshView);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);


        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String,String> hashMap = sessionManager.getUsersDetailFromSession();
        recyclerView = findViewById(R.id.orderHistorylist);
        refreshCircle = findViewById(R.id.refresh_circle);
        refreshCircle.setVisibility(View.GONE);

        recyclerView.setNestedScrollingEnabled(false);

        phoneNumber = hashMap.get(SessionManager.KEY_PHONENUMBER);
        setScrollEvent();
        makeRequest(FIRST);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setScrollEvent() {
        View.OnTouchListener li = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        if(refreshLayout.getChildAt(0).getBottom() == (refreshLayout.getHeight() + refreshLayout.getScrollY()) && !isLoading) {
                            Log.e("noMoreData", noMoreData+"");
                            if(noMoreData) {
                                refreshCircle.setVisibility(View.GONE);
                            }else {
                                refreshCircle.setVisibility(View.VISIBLE);
                                isLoading = true;
                                currentPos+=20;
                                makeRequest(AFTER_FIRST);
                            }
                            noMoreData = false;
                        }
                        break;
                }
                return false;
            }
        };
        refreshLayout.setOnTouchListener(li);

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
                        Log.e("response", response);
                        orderHistoryParsing = jsonParsing(response, state);
                        applyAdapter(orderHistoryParsing);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(OrderHistory.this, "잠시후에 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        refreshCircle.setVisibility(View.GONE);
                        progressApplication.progressOFF();
                    }
                });
        requestQueue.add(request);
    }


    private void applyAdapter(OrderHistoryParsing order) {
        if(orderHistoryParsing == null || orderHistoryParsing.getOrder() == null || orderHistoryParsing.order.size() == 0) {
            Toast.makeText(OrderHistory.this, "존재하는 주문내역이 없습니다.", Toast.LENGTH_SHORT).show();
        }else {
            orderHistoryAdapter = new OrderHistoryAdapter(order, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false));
            recyclerView.setAdapter(orderHistoryAdapter);
        }
        isLoading = false;
        progressApplication.progressOFF();
        refreshCircle.setVisibility(View.GONE);
    }

    private OrderHistoryParsing jsonParsing(String result, int state) {
        if(state == FIRST) {
            Gson gson = new Gson();
            orderHistoryParsing = gson.fromJson(result, OrderHistoryParsing.class);

            return orderHistoryParsing;
        }else {
            Gson gson = new Gson();
            OrderHistoryParsing addOrder = gson.fromJson(result, OrderHistoryParsing.class);
            if (!addOrder.getResult()) {
                Toast.makeText(this, "더 불러올 내역이 없습니다.", Toast.LENGTH_SHORT).show();
                noMoreData = true;

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
        if(!fromMyPage) {
            startActivity(new Intent(this, NewMainPage.class));
        }else {
            super.onBackPressed();
        }
        CustomIntent.customType(this,"right-to-left");
        finish();
    }
}