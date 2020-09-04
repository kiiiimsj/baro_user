package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.CouponAdapter;
import com.example.wantchu.AdapterHelper.Coupon;
import com.example.wantchu.AdapterHelper.CouponList;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class SideMyCoupon extends AppCompatActivity {
    //private final static String SERVER ="http://54.180.56.44:8080/";
    private final static int HEIGHT = 600;
    CouponAdapter couponAdapter;
    CouponList couponListData;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_my_coupon);
        recyclerView = findViewById(R.id.coupon_list);

        makeRequestForCoupon(urlMaker());
    }
    public String urlMaker() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();

        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.couponFindByPhone));
        urlBuilder.append(userData.get(SessionManager.KEY_PHONENUMBER));

        return urlBuilder.toString();
    }
    public void makeRequestForCoupon(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("couponList",response);
                        ParsingCoupon(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }

    private void ParsingCoupon(String response) {
        Gson gson = new Gson();
        couponListData = gson.fromJson(response, CouponList.class);
        if(!couponListData.result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(SideMyCoupon.this, "사용자 쿠폰이 존재하지 않습니다.", Toast.LENGTH_LONG);
                }
            });
            return;
        }
        setCouponList(couponListData);
    }

    private void setCouponList(CouponList couponListData) {
        couponAdapter = new CouponAdapter(couponListData);

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(couponAdapter);

//        setHeight();
    }

    private void setHeight() {
        LinearLayout.LayoutParams params= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = (couponListData.coupon.size() * HEIGHT);
        recyclerView.setLayoutParams(params);
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }
}