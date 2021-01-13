package com.tpn.baro.Fragment;

import android.content.Context;
import android.content.Intent;
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
import com.tpn.baro.Alerts;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;

import org.json.JSONException;
import org.json.JSONObject;

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
    public void onResume() {
        super.onResume();
        Log.e("makeRequestForAlerts", "onResume");
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
                if(!BaroUtil.loginCheck(getActivity())) {
                    return;
                }
                startActivity(new Intent(getContext(), Alerts.class));
            }
        });
        return rootView;
    }

    private void compareNumber() {
        if(getUnReadAlertCount == 0) {
            alarmBell.setImageResource(R.drawable.alert_off);
        }
        else if (getUnReadAlertCount > 0){
            alarmBell.setImageResource(R.drawable.alert_on);
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
