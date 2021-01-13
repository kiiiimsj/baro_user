package com.example.wantchu;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Celebe extends AppCompatActivity {
    private static String TAG = Celebe.class.getSimpleName();

    ImageView imageView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        imageView = findViewById(R.id.celebe_image);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://celebe.ohmyapp.io/bnb/aggregateForTable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"SUCCESS",response);
                        imageView.setImageURI(Uri.parse("http://celebe.ohmyapp.io/app-assets/images/portrait/small/avatar-a-1.png"));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG+"FAIL",error.toString());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
                params.put("metaCode","boardEvent");
                params.put("collectionName","boardEvent");
                params.put("documentJson", "{\"pipeline\":{\"$skip\":0,\"$limit\":10,\"$sort\":{\"_updateTime\":1}}}");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
}
