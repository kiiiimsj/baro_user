package com.example.wantchu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.OrderProgressingAdapter;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.JsonParsingHelper.OrderProgressingParsing;
import com.example.wantchu.JsonParsingHelper.OrderProgressingParsingHelper;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderProgressing extends AppCompatActivity {
    Gson gson;
    OrderProgressingParsing orderProgressingParsing;

    SwipyRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    OrderProgressingAdapter orderProgressingAdapter;
    String phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progressing);

        recyclerView = findViewById(R.id.orderProgresslist);
        refreshLayout = findViewById(R.id.refresh_list);
        gson = new Gson();
        SessionManager sessionManager = new SessionManager(this,SessionManager.SESSION_USERSESSION);
        HashMap<String,String> hashMap = sessionManager.getUsersDetailFromSession();
        phone = hashMap.get(SessionManager.KEY_PHONENUMBER);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                makeRequest(phone);
            }
        });

        makeRequest(phone);
    }

    private void makeRequest(String phone) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("OrderProgressing.do?phone=")+phone;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("success",response);
                        jsonparsing(response);
                        applyAdapter();
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

    private void applyAdapter() {
        ArrayList<OrderProgressingParsingHelper> orderProgressingParsingHelpers = orderProgressingParsing.getOrder();
        OrderProgressingAdapter orderProgressingAdapter = new OrderProgressingAdapter(orderProgressingParsingHelpers,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderProgressingAdapter);
        refreshLayout.setRefreshing(false);
    }

    private void jsonparsing(String response) {
        orderProgressingParsing = gson.fromJson(response,OrderProgressingParsing.class);
    }


    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
