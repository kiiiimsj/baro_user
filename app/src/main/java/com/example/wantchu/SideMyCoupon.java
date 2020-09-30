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
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

public class SideMyCoupon extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    CouponAdapter couponAdapter;
    CouponList couponListData;
    RecyclerView recyclerView;
    ProgressApplication progressApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_my_coupon);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        recyclerView = findViewById(R.id.coupon_list);
        makeRequestForCoupon(urlData());
    }
    public String urlData() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        userData.get(SessionManager.KEY_PHONENUMBER);
        return userData.get(SessionManager.KEY_PHONENUMBER);
    }
    public void makeRequestForCoupon(String phone) {
        String url = new UrlMaker().UrlMake("CouponFindByPhone.do?phone="+ phone);
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
                    progressApplication.progressOFF();
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
        progressApplication.progressOFF();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
    }
}