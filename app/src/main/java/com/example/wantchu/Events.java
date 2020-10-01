package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.JsonParsingHelper.EventDetailHelper;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

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

        eventDate = findViewById(R.id.event_date);
        eventImage = findViewById(R.id.event_image);
        eventContent = findViewById(R.id.event_content);
        fm = getSupportFragmentManager();
        topBar = (TopBar)fm.findFragmentById(R.id.top_bar);
        Intent intent =getIntent();
        storeId = intent.getIntExtra("event_id", 0);
        makeRequestForGetEvent();
    }
    @Override
    protected void onResume() {
        super.onResume();
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);
    }
    private void makeRequestForGetEvent() {
        String url = new UrlMaker().UrlMake("EventDetail.do?event_id="+storeId);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("storeInfo", response);
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
                        Log.i("response1", "response succeeded.");
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
    }
}