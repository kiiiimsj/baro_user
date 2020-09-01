package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.AdapterHelper.InquiryDetailData;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;

public class InquiryShow extends AppCompatActivity {

    int inquiryId;
    InquiryDetailData inquiryDetailData;
    TextView inquiryTitle;
    TextView inquiryDate;
    TextView inquiryIsReplied;
    TextView inquiryContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_show);
        Intent intent = getIntent();
        inquiryId = (Integer.parseInt(intent.getStringExtra("inquiryId")));
        inquiryTitle = findViewById(R.id.inquiry_title);
        inquiryDate = findViewById(R.id.inquiry_date);
        inquiryIsReplied = findViewById(R.id.inquiry_is_replied);
        inquiryContent = findViewById(R.id.inquiry_content);

        makeRequestForInquiryShow(urlMaker());
    }
    public String urlMaker() {
        ////http://54.180.56.44:8080/InquiryFindById.do?inquiry_id=문의내역id값
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplication().getString(R.string.inquiryFindById));
        urlBuilder.append(inquiryId);

        return urlBuilder.toString();
    }
    public void makeRequestForInquiryShow(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("INQUIRYLIST", response);
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
        inquiryDetailData = gson.fromJson(response, InquiryDetailData.class);
        drawContent(inquiryDetailData);
    }

    private void drawContent(InquiryDetailData data) {
        inquiryTitle.setText(data.getTitle());
        inquiryDate.setText(data.getInquiry_date());
        if(data.getIs_replied().equals("N")) {
            inquiryIsReplied.setText("답변이 미완료된 문의 입니다.");
        }
        if(data.getIs_replied().equals("Y")) {
            inquiryIsReplied.setText("답변이 완료된 문의 입니다.");
        }

        inquiryContent.setText(data.getContent());
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }
}