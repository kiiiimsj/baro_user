package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.AlertsAdapter;
import com.example.wantchu.AdapterHelper.AlertsHelperClass;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

public class Alerts extends AppCompatActivity {
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
    public void parsing(String response) {
        Gson gson = new Gson();
        eventsHelperData = gson.fromJson(response, AlertsHelperClass.class);
        saveAtUserSawAlarmList = getSharedPreferences("oldAlert", MODE_PRIVATE);
        SharedPreferences.Editor editor = saveAtUserSawAlarmList.edit();
        editor.putString("alertsList", response);
        editor.apply();
        editor.commit();
        Log.i("RESPONSE", saveAtUserSawAlarmList.toString());
        setRecyclerView();
    }
    public void setRecyclerView(){
        alertsAdapter = new AlertsAdapter(this, eventsHelperData);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        eventsRecyclerView.setAdapter(alertsAdapter);
        progressApplication.progressOFF();
    }
    public void onClickBack(View view) {
        super.onBackPressed();
    }
}