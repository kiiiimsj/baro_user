package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.CategoryListAdapter;
import com.example.wantchu.Adapter.InquiryListAdapter;
import com.example.wantchu.AdapterHelper.InquiryData;
import com.example.wantchu.AdapterHelper.InquiryDataList;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

import java.util.HashMap;

public class InquiryList extends AppCompatActivity implements InquiryListAdapter.OnListItemSelectedInterfaceForInquiry, InquiryListAdapter.OnListItemLongSelectedInterfaceForInquiry {
    //private final static String SERVER = "http://54.180.56.44:8080/";
    InquiryListAdapter inquiryListAdapter;
    InquiryDataList inquiryDataList;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_list);

        recyclerView = findViewById(R.id.inquiry_list_view);
        makeRequestForInquiry(urlMaker());
    }

    public void onClickBack(View view) {
        onBackPressed();
    }

    public void write_inquiry(View view) {
        Intent intent = new Intent(getApplicationContext(), Inquiry.class);
        startActivity(intent);
        finish();
    }
    public String urlMaker() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.inquiryListFindByEmail));
        urlBuilder.append(userData.get(SessionManager.KEY_EMAIL));

        return urlBuilder.toString();
    }

    public void makeRequestForInquiry(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        ParsingInquiry(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        requestQueue.add(request);
    }

    private void ParsingInquiry(String response) {
        Gson gson = new Gson();
        inquiryDataList = gson.fromJson(response, InquiryDataList.class);
        Log.i("GET GSON", inquiryDataList.toString());
        if(inquiryDataList.result == false) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(InquiryList.this, inquiryDataList.message, Toast.LENGTH_LONG);
                }
            });
            return;
        }
        setInquiryList(inquiryDataList);
    }

    private void setInquiryList(InquiryDataList data) {
        inquiryListAdapter = new InquiryListAdapter(data,this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(inquiryListAdapter);
    }

    @Override
    public void onItemSelectedForInquiry(View v, int position) {
        InquiryListAdapter.InquiryViewHolder viewHolder = (InquiryListAdapter.InquiryViewHolder)recyclerView.findViewHolderForAdapterPosition(position);
        TextView textView = (TextView)viewHolder.inquiryId;

        String inquiryId = textView.getText().toString();

        Intent intent = new Intent(getApplicationContext(), InquiryShow.class);
        intent.putExtra("inquiryId", inquiryId);
        startActivity(intent);
    }

    @Override
    public void onItemLongSelectedForInquiry(View v, int adapterPosition) {

    }
}