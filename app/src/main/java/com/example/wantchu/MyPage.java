package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.MyPageButtonListAdapter;
import com.example.wantchu.Adapter.MyPageButtonAdapter;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Dialogs.IfLogoutDialog;
import com.example.wantchu.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPage extends AppCompatActivity implements MyPageButtonListAdapter.OnListItemLongSelectedInterfaceForMyPage, MyPageButtonListAdapter.OnListItemSelectedInterfaceForMyPage, IfLogoutDialog.clickButton{
    int[] counts;
    RecyclerView mButtonlist;
    MyPageButtonListAdapter buttonAdapter;

    ArrayList<String> buttons;
    ArrayList<String> lists;

    String phone = null;


    RecyclerView buttonRecyclerViews;
    LinearLayout tableSize;

    TextView emailSpace;
    TextView phoneSpace;
    MyPageButtonAdapter myPageButtonAdapter;
    ProgressApplication progressApplication;
    RelativeLayout logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        setLists();
        counts = new int[buttons.size()];

        getPhoneNumber();
        makeRequestForOrderCount();


        mButtonlist = findViewById(R.id.button_list);
        buttonRecyclerViews = findViewById(R.id.menu_list);

        tableSize = findViewById(R.id.table_size);
        emailSpace = findViewById(R.id.email_space);
        phoneSpace = findViewById(R.id.phone_number);
        logout =findViewById(R.id.logout_button);
        setEvent();
        setMyInfo();
        setMyPageButtonRecyclerView();
        setExpandListener();
    }
    public void setLists() {
        buttons.add("주문내역");
        buttons.add("내 쿠폰");
        buttons.add("장바구니");

        lists.add("공지사항");
        lists.add("입점요청");
        lists.add("1:1 문의");
        lists.add("이용약관");
        lists.add("개인정보 처리방침");
    }
    private void setEvent() {
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new IfLogoutDialog(MyPage.this, MyPage.this).callFunction();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPhoneNumber();
        makeRequestForOrderCount();
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    private void setMyInfo() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        String name = userData.get(SessionManager.KEY_USERNAME);
        String email = userData.get(SessionManager.KEY_EMAIL);
        StringBuilder nameString = new StringBuilder(name + "님\n안녕하세요!");
        emailSpace.setText(email);
        phoneSpace.setText(phone);
    }

    private void setExpandListener() {
        myPageButtonAdapter = new MyPageButtonAdapter(this, lists);
        buttonRecyclerViews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        buttonRecyclerViews.setAdapter(myPageButtonAdapter);
    }

    private void getPhoneNumber() {
        SessionManager sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        phone = userData.get(SessionManager.KEY_PHONENUMBER);
    }
    public void setMyPageButtonRecyclerView() {
        myPageButtonAdapter = new MyPageButtonAdapter(getApplicationContext(), lists);
        buttonRecyclerViews.setAdapter(myPageButtonAdapter);
        progressApplication.progressOFF();
    }
    public void setMyPageButtonListRecyclerView(boolean result, int[] setCounts) {
        mButtonlist.setHasFixedSize(true);
        mButtonlist.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));
        if(result) {
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
    public void makeRequestForOrderCount() {
        String url = new UrlMaker().UrlMake("OrderTotalCountByPhone.do?phone="+phone);
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
    public void makeRequestForCouponCount() {
        String url = new UrlMaker().UrlMake("CouponCountByPhone.do?phone="+phone);
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
        makeRequestForCouponCount();
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
        setMyPageButtonListRecyclerView(result ,counts);
    }
    @Override
    public void clickOkay() {
        SessionManager sessionManager1 = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        if(sessionManager1.getUsersDetailFromSession() != null) {
            sessionManager1.clearDetailUserSession();
        }
        finish();
    }
    @Override
    public void clickCancel() {

    }
}
