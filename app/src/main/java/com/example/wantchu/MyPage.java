package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.MyPageButtonAdapter;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Dialogs.IfLogoutDialog;
import com.example.wantchu.Url.UrlMaker;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPage extends AppCompatActivity implements MyPageButtonAdapter.OnItemCilckListener, IfLogoutDialog.clickButton{
    final private String TAG = this.getClass().getSimpleName();
    int[] counts;
    ArrayList<String> lists;

    String phone = null;

    RecyclerView buttonRecyclerViews;
    RelativeLayout tableSize;

    TextView nameSpace;
    TextView phoneSpace;
    TextView emailSpace;

    TextView orderHistoryCount;
    TextView myCouponCount;
    TextView orderCartCount;

    RelativeLayout orderHistoryButton;
    RelativeLayout myCouponButton;
    RelativeLayout orderCartButton;

    MyPageButtonAdapter myPageButtonAdapter;
    ProgressApplication progressApplication;
    Button logout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        setLists();
        counts = new int[3];

        getPhoneNumber();
        makeRequestForOrderCount();


        buttonRecyclerViews = findViewById(R.id.menu_list);

        tableSize = findViewById(R.id.table_size);

        nameSpace= findViewById(R.id.user_name_space);
        phoneSpace = findViewById(R.id.phone);
        emailSpace = findViewById(R.id.email);

        orderHistoryCount = findViewById(R.id.order_history_count);
        myCouponCount = findViewById(R.id.my_coupon_count);
        orderCartCount = findViewById(R.id.my_order_cart_count);

        orderHistoryButton = findViewById(R.id.order_history_button);
        myCouponButton = findViewById(R.id.my_coupon_button);
        orderCartButton = findViewById(R.id.my_order_cart_button);

        logout = findViewById(R.id.logout);
        setEvent();
        setMyInfo();
        setExpandListener();
    }

    private void setButtons() {
        SharedPreferences shf = getSharedPreferences("basketList", MODE_PRIVATE);
        counts[2] = shf.getInt("orderCnt", 0);

        orderHistoryCount.setText(counts[0]+" 건");
        myCouponCount.setText(counts[1]+ " 건");
        orderCartCount.setText(counts[2]+ " 건");
    }

    public void setLists() {
        lists = new ArrayList<>();
        lists.add("공지사항");
        lists.add("입점요청");
        lists.add("1:1 문의");
        lists.add("비밀번호 변경");
        lists.add("이메일 변경");
        lists.add("이용약관");
        lists.add("개인정보 처리방침");
    }
    private void setEvent() {
        orderHistoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyPage.this, OrderHistory.class));
            }
        });
        myCouponButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyPage.this, SideMyCoupon.class));
            }
        });
        orderCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences shf = getSharedPreferences("basketList", MODE_PRIVATE);
                if(shf.getInt("orderCnt", 0) > 0) {
                    startActivity(new Intent(MyPage.this, Basket.class));
                }
            }
        });
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
        setMyInfo();
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    private void setMyInfo() {
        SessionManager sessionManager = new SessionManager(MyPage.this, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        String name = userData.get(SessionManager.KEY_USERNAME);
        String email = userData.get(SessionManager.KEY_EMAIL);
        StringBuilder nameString = new StringBuilder(name + "님");
        nameSpace.setText(nameString.toString());
        emailSpace.setText(email);
        phoneSpace.setText(phone);
    }

    private void setExpandListener() {
        myPageButtonAdapter = new MyPageButtonAdapter(this, lists, this);
        buttonRecyclerViews.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        buttonRecyclerViews.setAdapter(myPageButtonAdapter);
        progressApplication.progressOFF();
    }

    private void getPhoneNumber() {
        SessionManager sessionManager = new SessionManager(MyPage.this, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersDetailSession();
        HashMap<String, String> userData = sessionManager.getUsersDetailFromSession();
        phone = userData.get(SessionManager.KEY_PHONENUMBER);
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
        setButtons();
    }
    @Override
    public void clickOkay() {
        SessionManager sessionManager1 = new SessionManager(MyPage.this, SessionManager.SESSION_USERSESSION);
        if(sessionManager1.getUsersDetailFromSession() != null) {
            sessionManager1.clearDetailUserSession();
        }
        finish();
    }
    @Override
    public void clickCancel() {

    }

    @Override
    public void itemClick(int position) {
        switch (position) {
            case 0 :
                startActivity(new Intent(MyPage.this, Notice.class));
                break;
            case 1 :
            case 2 :
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_bYeuK/chat")));
                break;
            case 3:
                startActivity(new Intent(MyPage.this,ChangePass1Logging.class));
                break;
            case 4:
                startActivity(new Intent(MyPage.this, ChangeEmail.class));
                break;
            case 5 :
            case 6 :
                startActivity(new Intent(MyPage.this, TermsOfUse.class));
                break;
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainPage.class));
        finish();
    }
}
