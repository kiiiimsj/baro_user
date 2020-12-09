package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Alert extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    Intent getAlert;
    int alertId;
    String alertTitleString;
    String alertStartDateString;
    TextView alertTitle;
    TextView alertStartDate;
    TextView alertContent;

    SessionManager sessionManager;
    HashMap userData = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);
        getAlert = getIntent();
        alertId = getAlert.getIntExtra("alertId", 0);
        alertTitleString = getAlert.getStringExtra("alertTitle");
        alertStartDateString = getAlert.getStringExtra("alertStartDate");

        alertTitle = findViewById(R.id.alert_title);
        alertStartDate = findViewById(R.id.alert_start_date);
        alertContent = findViewById(R.id.alert_content);

        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        userData = sessionManager.getUsersDetailFromSession();

        makeRequestForAlertDetail();
        makeRequestForAlertRead();
    }

    private void makeRequestForAlertRead() {
        UrlMaker urlMaker = new UrlMaker();
        Log.e("alertReadCheck : ", alertId + "" + userData.get(SessionManager.KEY_PHONENUMBER));
        String url=urlMaker.UrlMake("AlertReadCheck.do?alert_id="+alertId+"&phone="+userData.get(SessionManager.KEY_PHONENUMBER));
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("sendToRead", response);
                parsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void makeRequestForAlertDetail() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("GetAlertDetail.do?alert_id="+alertId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getOldEvents", response);
                parsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }
    private void parsing(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getBoolean("result")) {
                settingView(jsonObject.getString("content"));
            }
        }
        catch (JSONException e) {}
    }

    private void settingView(String content) {
        alertTitle.setText(alertTitleString);
        alertStartDate.setText(alertStartDateString);
        alertContent.setText(content);
    }

    @Override
    public void onBack() {
        super.onBackPressed();
    }
}