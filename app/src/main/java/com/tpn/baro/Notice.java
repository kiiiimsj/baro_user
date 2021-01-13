package com.tpn.baro;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ExpandableListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.ExpandAdapter;
import com.tpn.baro.AdapterHelper.NoticeGroup;
//import com.example.baro.Database.SendToServer;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.NoticeListParsing;
import com.tpn.baro.JsonParsingHelper.NoticeParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class Notice extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity {
    ArrayList<NoticeGroup> DataList;
    private ExpandableListView listView;
    int width;
    int openChild = 0;
    ProgressApplication progressApplication;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;

        DataList = new ArrayList<NoticeGroup>();

        listView = (ExpandableListView)findViewById(R.id.NoticeList);

        makeRequest();
    }


    private NoticeParsing jsonParsing(String result) {
        NoticeParsing noticeParsing = new NoticeParsing();
        try {
            Boolean result1 = (Boolean)new JSONObject(result).getBoolean("result");
            if(!result1) {
                progressApplication.progressOFF();
            }
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

    private void makeRequest() {
        String url = new UrlMaker().UrlMake("NoticeFindByCode.do?notice_code=NOTICE");
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.GET, url,
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
                new ExpandAdapter(Notice.this,R.layout.activity_notice_group_parent,R.layout.activity_notice_group_child,DataList);
        listView.setIndicatorBounds(width-100, width); //이 코드를 지우면 화살표 위치가 바뀐다.

        listView.setAdapter(adapter);
        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(final int i) {
                final Animation animation = AnimationUtils.loadAnimation(Notice.this,R.anim.expandable_open);
                animation.setDuration(600);
                openChild+=1;
                Log.e("i",i+openChild+"");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        try {
                            Thread.sleep(100);
                            listView.getChildAt(i+openChild).setAnimation(animation);
                            listView.getChildAt(i+openChild).startAnimation(animation);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }, animation.getDuration());
            }
        });
        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                Animation animation = AnimationUtils.loadAnimation(Notice.this,R.anim.expandable_open);
                animation.setDuration(600);
                openChild-=1;
            }
        });
        progressApplication.progressOFF();
    }
    @Override
    public void onBack() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }
}