package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.AlertsAdapter;
import com.example.wantchu.AdapterHelper.AlertsHelperClass;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.HashMap;

public class Alerts extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity, AlertsAdapter.ClickAlert {
    RecyclerView alertsRecyclerView;
    ProgressApplication progressApplication;
    AlertsHelperClass alertsHelperClass;
    AlertsAdapter alertsAdapter;

    SessionManager userInfo;
    HashMap userData = new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_list);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        alertsRecyclerView = findViewById(R.id.alert_list);
        userInfo = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        userData = userInfo.getUsersDetailFromSession();
    }

    @Override
    protected void onResume() {
        super.onResume();
        makeRequestForAlerts();
    }

    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("AlertFindAll.do?phone="+userData.get(SessionManager.KEY_PHONENUMBER));
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
    public void parsing(String response) {
        Gson gson = new Gson();
        alertsHelperClass = gson.fromJson(response, AlertsHelperClass.class);
        if(!alertsHelperClass.result) {
            Toast.makeText(this,"로딩 실패", Toast.LENGTH_LONG).show();
            progressApplication.progressOFF();
            return;
        }
        setRecyclerView();
    }
    public void setRecyclerView(){
        alertsAdapter = new AlertsAdapter(this, alertsHelperClass, this);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        alertsRecyclerView.setAdapter(alertsAdapter);
        progressApplication.progressOFF();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
    }

    @Override
    public void clickAlertListener(int alertId, int pos) {
        Intent intent = new Intent(getApplicationContext(), Alert.class);
        intent.putExtra("alertId", alertId);
        intent.putExtra("alertTitle", alertsHelperClass.getAlert().get(pos).alert_title);
        intent.putExtra("alertStartDate", alertsHelperClass.getAlert().get(pos).alert_startdate);
        startActivity(intent);
    }
}