package com.tpn.baro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tpn.baro.Adapter.OrderProgressingAdapter;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.OrderProgressingParsing;
import com.tpn.baro.JsonParsingHelper.OrderProgressingParsingHelper;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderProgressing extends AppCompatActivity implements TopBar.ClickButton {
    final private String TAG = this.getClass().getSimpleName();
    Gson gson;
    OrderProgressingParsing orderProgressingParsing;

    SwipyRefreshLayout refreshLayout;
    RecyclerView recyclerView;
    OrderProgressingAdapter orderProgressingAdapter;
    String phone;
    ProgressApplication progressApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_progressing);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);

        recyclerView = findViewById(R.id.orderProgresslist);
        refreshLayout = findViewById(R.id.refresh_list);
        gson = new Gson();
        SessionManager sessionManager = new SessionManager(getApplicationContext(),SessionManager.SESSION_USERSESSION);
        HashMap<String,String> hashMap = sessionManager.getUsersDetailFromSession();
        phone = hashMap.get(SessionManager.KEY_PHONENUMBER);
        refreshLayout.setDistanceToTriggerSync(20);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                makeRequest(phone);
            }
        });


        makeRequest(phone);
    }
    private void makeRequest(String phone) {
        orderProgressingParsing = new OrderProgressingParsing();
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("OrderProgressing.do?phone=")+phone;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("response", response);
                        jsonparsing(response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("fail",error.toString());
                        Toast.makeText(OrderProgressing.this, "잠시후에 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                        progressApplication.progressOFF();
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
        progressApplication.progressOFF();
    }

    private void jsonparsing(String response) {
        orderProgressingParsing = gson.fromJson(response,OrderProgressingParsing.class);
        if(orderProgressingParsing == null || orderProgressingParsing.getOrder() == null || orderProgressingParsing.getOrder().size() == 0) {
            Toast.makeText(OrderProgressing.this, "존재하는 주문현황이 없습니다.", Toast.LENGTH_SHORT).show();
            progressApplication.progressOFF();
        }else {
            applyAdapter();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, NewMainPage.class));
        finish();
    }

    @Override
    public void clickButton() {
         makeRequest(phone);
    }
}
