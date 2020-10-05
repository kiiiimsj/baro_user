package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.AlertsAdapter;
import com.example.wantchu.AdapterHelper.AlertsHelperClass;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.JsonParsingHelper.AlertIsNewParsing;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

public class Alerts extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    RecyclerView eventsRecyclerView;
    ProgressApplication progressApplication;
    AlertsHelperClass eventsHelperData;
    AlertsAdapter alertsAdapter;
    SharedPreferences saveAtUserSawAlarmList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_list);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        eventsRecyclerView = findViewById(R.id.alert_list);

        makeRequestForAlerts();
        makeRequestForAlertsGetId();
    }

    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("AlertFindAll.do");
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
    private void makeRequestForAlertsGetId() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("GetLatestAlertWhenMemberLogin.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("AlertId", response);
                alertIdParsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(stringRequest);
    }

    private void alertIdParsing(String response) {
        Gson gson = new Gson();
        AlertIsNewParsing parsing = gson.fromJson(response, AlertIsNewParsing.class);
        saveAtUserSawAlarmList = getSharedPreferences("oldAlert", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveAtUserSawAlarmList.edit();
        editor.putInt("alertId", parsing.getRecentlyAlertId());
        editor.apply();
        editor.commit();
    }

    public void parsing(String response) {
        Gson gson = new Gson();
        eventsHelperData = gson.fromJson(response, AlertsHelperClass.class);
        if(!eventsHelperData.result) {
            Toast.makeText(this,"로딩 실패", Toast.LENGTH_LONG).show();
            progressApplication.progressOFF();
            return;
        }
        setRecyclerView();
    }
    public void setRecyclerView(){
        alertsAdapter = new AlertsAdapter(this, eventsHelperData);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsRecyclerView.setAdapter(alertsAdapter);
        progressApplication.progressOFF();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
    }
}