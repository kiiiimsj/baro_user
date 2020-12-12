package com.example.wantchu.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Alerts;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class AlarmBell extends Fragment {
    public Context context;
    public ViewGroup rootView;
    public ImageView alarmBell;

    int getUnReadAlertCount = 0;

    SessionManager userSession;
    HashMap userData = new HashMap<>();
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userSession = new SessionManager(getContext(), SessionManager.SESSION_USERSESSION);
        userData = userSession.getUsersDetailFromSession();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        rootView = (ViewGroup) inflater.inflate(R.layout.alarm_bell, container, false);
        alarmBell = rootView.findViewById(R.id.alarm_bell);
        alarmBell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), Alerts.class));
            }
        });
        return rootView;
    }

    private void compareNumber() {
        if(getUnReadAlertCount == 0) {
            alarmBell.setImageResource(R.drawable.off);
        }
        else if (getUnReadAlertCount > 0){
            alarmBell.setImageResource(R.drawable.on);
        }
    }

    private void makeRequestForAlerts() {
        UrlMaker urlMaker = new UrlMaker();
        Log.e("getAlertCount", userData.get(SessionManager.KEY_PHONENUMBER)+"");
        String url = urlMaker.UrlMake("GetNewAlertCount.do?phone=" + userData.get(SessionManager.KEY_PHONENUMBER));
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("getAlertCount", response);
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
                getUnReadAlertCount = jsonObject.getInt("count");
            }
        }catch (JSONException e) {

        }
        compareNumber();
    }
}
