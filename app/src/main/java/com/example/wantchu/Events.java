package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Url.UrlMaker;

public class Events extends AppCompatActivity {
    RecyclerView eventsRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        eventsRecyclerView = findViewById(R.id.event_list);
        makeRequestForEvents();
    }
    private void makeRequestForEvents() {
        UrlMaker urlMaker = new UrlMaker();
        String url=urlMaker.UrlMake("");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

    }
    public void parsing(String response) {

    }
    public void setRecyclerView(){

    }
    public void onClickBack(View view) {
        super.onBackPressed();
    }
}