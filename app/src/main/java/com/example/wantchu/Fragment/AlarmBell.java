package com.example.wantchu.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.AdapterHelper.AlertsHelperClass;
import com.example.wantchu.Alerts;
import com.example.wantchu.JsonParsingHelper.AlertIsNewParsing;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

public class AlarmBell extends Fragment {
    public Context context;
    public ViewGroup rootView;
    public TextView isNewAlarm;
    public ImageView alarmBell;

    int saveAlertNumber = 0 ;
    int saveNewAlertNumber = 0;

    SharedPreferences oldSp;

    AlertIsNewParsing newNumber;

    public AlarmBell(Context context) {
        this.context = context;
    }
    public AlarmBell() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        makeRequestForAlerts();
    }

    @Override
    public void onResume() {
        super.onResume();
        makeRequestForAlerts();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.alarm_bell, container, false);
        isNewAlarm = rootView.findViewById(R.id.get_alarm);
        alarmBell = rootView.findViewById(R.id.alarm_bell);
        makeRequestForAlerts();

        alarmBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, Alerts.class));
            }
        });
        return rootView;
    }

    private void compareNumber() {
        if(saveNewAlertNumber == saveAlertNumber) {
            isNewAlarm.setVisibility(View.INVISIBLE);
        }
        else {
            isNewAlarm.setVisibility(View.VISIBLE);
        }
    }

    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("GetLatestAlertWhenMemberLogin.do");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getEvents", response);
                compareSetNumber(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void compareSetNumber(String response) {
        oldSp = getActivity().getSharedPreferences("oldAlert", Context.MODE_PRIVATE);
        if(oldSp.getInt("alertId", 0) == 0) {
            saveAlertNumber = 0;
        }
        else {
            saveAlertNumber = oldSp.getInt("alertId", 0);
        }

        Gson gson2 = new Gson();
        newNumber = gson2.fromJson(response, AlertIsNewParsing.class);
        Log.i("newNumber", newNumber.toString());
        saveNewAlertNumber = newNumber.getRecentlyAlertId();

        Log.i("NEW_ALERT_NUMBER", saveNewAlertNumber+"");
        Log.i("OLD_ALERT_NUMBER", saveAlertNumber+"");
        compareNumber();
    }
}
