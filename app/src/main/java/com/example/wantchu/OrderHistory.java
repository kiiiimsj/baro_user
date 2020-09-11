package com.example.wantchu;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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
import com.example.wantchu.Adapter.OrderHistoryAdapter;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Fragment.HistoryDetail;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsing;
import com.example.wantchu.JsonParsingHelper.OrderHistoryParsingHelper;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderHistory extends AppCompatActivity {
    String phoneNumber;
    SessionManager sessionManager;
    OrderHistoryParsing orderHistoryParsing;
    RecyclerView recyclerView;
    OrderHistoryAdapter orderHistoryAdapter;
    HistoryDetail historyDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String,String> hashMap = sessionManager.getUsersDetailFromSession();
//        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        if (sessionManager == null) {
            Log.i("Error update Session", sessionManager.toString());
        }
        phoneNumber = hashMap.get(SessionManager.KEY_PHONENUMBER);
//        phoneNumber = sessionManager.getUsersSession().getString(SessionManager.KEY_PHONENUMBER, "");
        Log.i("sdffd", phoneNumber + "zzzzzzzzzzz");
        recyclerView = findViewById(R.id.orderHistorylist);
        makeRequest();
        startProgress();

    }
    private void startProgress(){
        final ProgressApplication progressApplication = new ProgressApplication();

        progressApplication.progressON(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressApplication.progressOFF();
            }
        },3500);
    }
    private synchronized void makeRequest() {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "OrderListFindByPhone.do?phone=" + phoneNumber;
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(final String response) {
                        Log.i("OrderHistory", response);
                        orderHistoryParsing = jsonParsing(response);
                        applyAdapter(orderHistoryParsing.getOrder());
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

    private void applyAdapter(ArrayList<OrderHistoryParsingHelper> order) {
        orderHistoryAdapter = new OrderHistoryAdapter(order, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderHistory.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(orderHistoryAdapter);
    }

    private OrderHistoryParsing jsonParsing(String result) {
        Gson gson = new Gson();
        orderHistoryParsing = gson.fromJson(result, OrderHistoryParsing.class);
        Log.i("s", "시발 되나?");
        return orderHistoryParsing;
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}