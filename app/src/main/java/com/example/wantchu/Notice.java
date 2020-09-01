package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.ExpandAdapter;
import com.example.wantchu.AdapterHelper.NoticeGroup;
//import com.example.wantchu.Database.SendToServer;
import com.example.wantchu.HelperDatabase.LoginUser;
import com.example.wantchu.JsonParsingHelper.NoticeListParsing;
import com.example.wantchu.JsonParsingHelper.NoticeParsing;
import com.example.wantchu.JsonParsingHelper.ResponseToNoticeParsing;
import com.example.wantchu.Url.UrlMaker;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Notice extends AppCompatActivity {
    TextView mainTitle;
    String uri;
    ArrayList<NoticeGroup> DataList;
    private ExpandableListView listView;
    int width;
    @Override
    public void  onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_notice);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        
        mainTitle = findViewById(R.id.mainTitle);

        Intent intent = getIntent();
        String type = intent.getExtras().getString("type");

        DataList = new ArrayList<NoticeGroup>();
        listView = (ExpandableListView)findViewById(R.id.NoticeList);

        HashMap<String,NoticeGroup> hashMap = new HashMap<>();
        hashMap.put("NoticeGroup",new NoticeGroup(""));

        if(type.equals("NOTICE")){
            mainTitle.setText("공지 사항");
            uri="NoticeFindByCode.do?notice_code=NOTICE";
            makeRequest(uri);
        }
        else if(type.equals("ALERT")){
            mainTitle.setText("알 림");
            uri="NoticeFindByCode.do?notice_code=ALERT";
            makeRequest(uri);
        }
        else{
            mainTitle.setText("딴 거");
            uri="NoticeFindAll.do";
            makeRequest(uri);
        }
    }

    private synchronized NoticeParsing jsonParsing(String result) {
        NoticeParsing noticeParsing = new NoticeParsing();
        try {

            Boolean result1 = (Boolean)new JSONObject(result).getBoolean("result");
            noticeParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("notice");
            ArrayList<NoticeListParsing> noticeListParsings = new ArrayList<NoticeListParsing>();
            for (int i = 0; i < jsonArray.length(); i++) {
                HashMap map = new HashMap<>();
                JSONObject jObject = jsonArray.getJSONObject(i);
                String notice_code = jObject.optString("notice_code");
                String notice_date = jObject.optString("notice_date");
                String title = jObject.optString("title");
                int notice_id = jObject.optInt("notice_id");
                String content = jObject.optString("content");
                NoticeListParsing noticeListParsing = new NoticeListParsing(notice_code,notice_date,title,notice_id,content);
                noticeListParsings.add(noticeListParsing);
            }
            noticeParsing.setNoticeList(noticeListParsings);
            Log.i("DSF",result1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return noticeParsing;
    }

    private synchronized void makeRequest(String uri) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(uri);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, urlBuilder.toString(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("OrderDetails", response);
                        NoticeParsing noticeParsing = jsonParsing(response);
                        applyAdapter(noticeParsing);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("OrderDetails", "error");
                    }
                });
        requestQueue.add(request);
    }
    private void applyAdapter(NoticeParsing noticeParsing){
        for(int i = 0; i<noticeParsing.getNoticeList().size();i++){
            NoticeListParsing noticeListParsing = noticeParsing.getNoticeList().get(i);
            NoticeGroup noticeGroup = new NoticeGroup(noticeListParsing.getTitle());
            noticeGroup.childContent.add(noticeListParsing.getContent());
            noticeGroup.childTitle.add(noticeListParsing.getTitle());
            noticeGroup.childDate.add(noticeListParsing.getNotice_date());
            DataList.add(noticeGroup);
        }

        ExpandAdapter adapter =
                new ExpandAdapter(getApplicationContext(),R.layout.activity_notice_group_parent,R.layout.activity_notice_group_child,DataList);
        listView.setIndicatorBounds(width-100, width); //이 코드를 지우면 화살표 위치가 바뀐다.
        listView.setAdapter(adapter);
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }
}