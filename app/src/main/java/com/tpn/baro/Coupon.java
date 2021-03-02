package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.CouponAdapter;
import com.tpn.baro.AdapterHelper.CouponList;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.Dialogs.CouponRegisterDialog;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.DefaultParsing;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class Coupon extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity, CouponRegisterDialog.OnDismiss {
    private final String TAG = this.getClass().getSimpleName();
    CouponAdapter couponAdapter;
    CouponList couponListData;
    RecyclerView recyclerView;
    ProgressApplication progressApplication;
    Button registerBtn;
    EditText couponInput;
    Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        gson = new Gson();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_side_my_coupon);
        registerBtn = findViewById(R.id.registerBtn);
        couponInput = findViewById(R.id.coupon_number_input);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (couponInput.getText().toString().equals("")) {
                    return;
                }
                String phone = urlData();
                String url = new UrlMaker().UrlMake("CouponInsertByNumber.do?phone="+phone+"&coupon_id="+couponInput.getText());

                RequestQueue requestQueue = Volley.newRequestQueue(Coupon.this);

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                DefaultParsing defaultParsing = gson.fromJson(response,DefaultParsing.class);
                                CouponRegisterDialog couponRegisterDialog = new CouponRegisterDialog(Coupon.this, Coupon.this,defaultParsing);
                                couponRegisterDialog.callFunction();
                                makeRequestForCoupon(urlData());
                            }
                        },
                        new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG,error.toString());
                    }
                });
                requestQueue.add(stringRequest);
            }
        });
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

        couponListData = gson.fromJson(response, CouponList.class);
        if(!couponListData.result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(Coupon.this, "사용자 쿠폰이 존재하지 않습니다.", Toast.LENGTH_LONG);
                    progressApplication.progressOFF();
                }
            });
            return;
        }
        setCouponList(couponListData);
    }

    private void setCouponList(CouponList couponListData) {
        couponAdapter = new CouponAdapter(couponListData);
        recyclerView.setLayoutManager(new LinearLayoutManager(Coupon.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(couponAdapter);
        progressApplication.progressOFF();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }

    @Override
    public void clickDismiss() {

    }
}