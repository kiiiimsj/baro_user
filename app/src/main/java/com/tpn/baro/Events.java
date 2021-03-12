package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.EventDetailHelper;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.google.gson.Gson;
import com.tpn.baro.helperClass.BaroUtil;

import maes.tech.intentanim.CustomIntent;

public class Events extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    int storeId;
    TextView eventDate;
    TextView eventContent;
    ImageView eventImage;
    EventDetailHelper eventDetailHelperData;
    FragmentManager fm;
    TopBar topBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(Events.this, this.toString());
        }
        eventDate = findViewById(R.id.event_date);
        eventImage = findViewById(R.id.event_image);
        eventContent = findViewById(R.id.event_content);
        fm = getSupportFragmentManager();
        topBar = (TopBar)fm.findFragmentById(R.id.top_bar);
        Intent intent =getIntent();
        storeId = intent.getIntExtra("event_id", 0);
        makeRequestForGetEvent();
    }
    private void makeRequestForGetEvent() {
        String url = new UrlMaker().UrlMake("EventDetail.do?event_id="+storeId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
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
        Gson gson = new Gson();
        eventDetailHelperData =gson.fromJson(response, EventDetailHelper.class);
        setView();
    }

    private void setView() {
        topBar.setTitleStringWhereUsedEventsAndListStore(eventDetailHelperData.event_title);
        eventDate.setText(eventDetailHelperData.event_startdate + " ~ " + eventDetailHelperData.event_enddate);
        eventContent.setText(eventDetailHelperData.event_content);
        fm.beginTransaction().show(topBar);
        makeRequestForgetImage(eventDetailHelperData.event_image, eventImage, this);
    }
    public void makeRequestForgetImage(String type_image, final ImageView imageView, Context context ) {
        String url = new UrlMaker().UrlMake("ImageEvent.do?image_name="+type_image);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 300, 300, ImageView.ScaleType.FIT_END, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(request);
    }
    @Override
    public void onBack() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }
}