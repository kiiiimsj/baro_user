package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.MyPageButtonListAdapter;
import com.example.wantchu.Adapter.MyPageExpandAdapter;
import com.example.wantchu.AdapterHelper.MyPageListbuttons;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPage extends AppCompatActivity implements MyPageButtonListAdapter.OnListItemLongSelectedInterfaceForMyPage, MyPageButtonListAdapter.OnListItemSelectedInterfaceForMyPage {
    //private final static String SERVER = "http://54.180.56.44:8080/";
    private final static int ORDER = 1;
    private final static int COUPON = 2;
    int[] counts;
    RecyclerView mButtonlist;
    MyPageButtonListAdapter buttonAdapter;

    String[] buttons = {"주문내역", "내 쿠폰", "장바구니"};

    String phone = null;
    ArrayList<MyPageListbuttons> lists;

    ExpandableListView expandableListView;
    LinearLayout tableSize;

    //TextView nameSpace;
    TextView emailSpace;
    TextView phoneSpace;
    MyPageExpandAdapter myPageExpandAdapter;
    ProgressApplication progressApplication;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        counts = new int[3];

        getPhoneNumber();
        makeRequestForOrderCount(urlMaker());


        mButtonlist = findViewById(R.id.button_list);
        expandableListView = findViewById(R.id.expandable_list);

        tableSize = findViewById(R.id.table_size);

        //nameSpace = findViewById(R.id.name_space);
        emailSpace = findViewById(R.id.email_space);
        phoneSpace = findViewById(R.id.phone_number);

        setMyInfo();
        setList();
        setExpandableListView();
        setExpandListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhoneNumber();
        makeRequestForOrderCount(urlMaker());
    }

    private void setMyInfo() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        String name = userData.get(SessionManager.KEY_USERNAME);
        String email = userData.get(SessionManager.KEY_EMAIL);
        StringBuilder nameString = new StringBuilder(name + "님\n안녕하세요!");
        //nameSpace.setText(nameString.toString());
        emailSpace.setText(email);
        phoneSpace.setText(phone);
    }

    private void setExpandListener() {
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(groupPosition == 1) {
                    startActivity(new Intent(getApplicationContext(), SideMyCoupon.class));
                }
                if(groupPosition == 2) {
                    Intent intent = new Intent(getApplicationContext(), Notice.class);
                    intent.putExtra("type", "NOTICE");
                    startActivity(intent);
                }
                if(groupPosition == 3) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_bYeuK/chat")));
                }
                if(groupPosition == 4) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_bYeuK/chat")));
                }
                return false;
            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if(groupPosition == 0 ) {
                    if (childPosition == 0) {
                        startActivity(new Intent(getApplicationContext(), ChangePass1Logging.class));
                    }
                    if (childPosition == 1) {
                        startActivity(new Intent(getApplicationContext(), ChangeEmail.class));
                    }
                }
                return false;
            }
        });
    }

    private void getPhoneNumber() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        phone = userData.get(SessionManager.KEY_PHONENUMBER);
    }

    public void setList() {
        MyPageListbuttons list;
        MyPageListbuttons list1;
        MyPageListbuttons list2;
        MyPageListbuttons list3;
        MyPageListbuttons list4;

        list = new MyPageListbuttons("내 정보 변경");

        list.childText.add("비밀번호 변경");
        list.childText.add("이메일 변경");

        list1 = new MyPageListbuttons("쿠폰");

        list2 = new MyPageListbuttons("공지사항");

        list3 = new MyPageListbuttons("입점요청");

        list4= new MyPageListbuttons("1:1문의");

        lists = new ArrayList<MyPageListbuttons>();

        lists.add(0, list);
        lists.add(1, list1);
        lists.add(2, list2);
        lists.add(3, list3);
        lists.add(4, list4);
    }
    public void setExpandableListView() {
        myPageExpandAdapter = new MyPageExpandAdapter(getApplicationContext(), R.layout.my_page_parent_view, R.layout.my_page_expandable, lists);
        expandableListView.setGroupIndicator(null);
        expandableListView.setAdapter(myPageExpandAdapter);
        progressApplication.progressOFF();
    }
    public void setRecyclerView(boolean result, int[] setCounts) {
        mButtonlist.setHasFixedSize(true);

        mButtonlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        if(result) {
            Log.i("VIEW", result+"");
            Log.i("VIEW", setCounts+"");
            buttonAdapter = new MyPageButtonListAdapter(this, buttons, setCounts, this);
        }
        else {
            buttonAdapter = new MyPageButtonListAdapter(buttons);
        }
        mButtonlist.setAdapter(buttonAdapter);
    }
    @Override
    public void onItemLongSelectedForMyPage(View v, int adapterPosition) {

    }
    @Override
    public void onItemSelectedForMyPage(View v, int position) {

        if(position == 0) {
            startActivity(new Intent(getApplicationContext(), OrderHistory.class));
        }
        if(position == 1) {
            startActivity(new Intent(getApplicationContext(), SideMyCoupon.class));
        }
        if(position == 2) {
            SharedPreferences shf = getSharedPreferences("basketList", MODE_PRIVATE);
            if(shf.getInt("orderCnt", 0) > 0) {
                startActivity(new Intent(getApplicationContext(), Basket.class));
            }
        }
    }
    public String urlMaker() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.orderTotalCountByPhone));
        urlBuilder.append(phone);

        return urlBuilder.toString();
    }
    public String urlMaker2() {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append(getApplicationContext().getString(R.string.couponCountByPhone));
        urlBuilder.append(phone);

        return urlBuilder.toString();
    }
    public void makeRequestForOrderCount(final String url) {
        //http://54.180.56.44:8080/OrderTotalCountByPhone.do?phone=?
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("type", "request made to " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("data", response);
                        jsonParsingOrderCount(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void makeRequestForCouponCount(final String url) {
        //http://54.180.56.44:8080/CouponCountByPhone.do?phone=전화번호
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("type", "request made to " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("data", response);
                        jsonParsingCouponCount(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }


    private void jsonParsingOrderCount(String response) {
        boolean result = false;
        int count = 0;
        try {
            JSONObject jsonObject = new JSONObject(response);
            result=jsonObject.getBoolean("result");
            count=jsonObject.getInt("total_orders");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        counts[0] = count;
        makeRequestForCouponCount(urlMaker2());
    }
    private void jsonParsingCouponCount(String response) {
        boolean result = false;
        int count = 0;
        try {
            JSONObject jsonObject = new JSONObject(response);
            result=jsonObject.getBoolean("result");
            count=jsonObject.getInt("coupon_count");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        counts[1] = count;
        setRecyclerView(result ,counts);
    }
}
