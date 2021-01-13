package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.Dialogs.AddFavoriteDialog;
import com.example.wantchu.Dialogs.DeleteFavoriteDialog;
import com.example.wantchu.Fragment.StoreDetailInfoFragment;
import com.example.wantchu.Fragment.StoreMenuFragment;
import com.example.wantchu.Fragment.TopBar;
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.JsonParsingHelper.FavoriteListParsing;
import com.example.wantchu.JsonParsingHelper.FavoriteParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.BaroUtil;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

public class StoreInfoReNewer extends AppCompatActivity implements TopBar.OnBackPressedInParentActivity, TopBar.ClickImage {
    SessionManager sessionManager;
    String _phone;
    String storedIdStr;
    TabLayout tabs;
    TextView tabMenu;
    TextView tabStoreInfo;

    boolean result =false;

    SharedPreferences sp;
    StoreMenuFragment storeMenuFragment;
    StoreDetailInfoFragment storeDetailInfoFragment;
    StoreDetail storeDetailData;
    Intent myIntent;

    FragmentManager fm;
    TopBar topBar;
    ImageView.OnClickListener heartClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info_re_newer);
        setOnClickFavorite();
        fm = getSupportFragmentManager();
        topBar = (TopBar) fm.findFragmentById(R.id.top_bar);
        tabs = findViewById(R.id.tab_tabs);
        tabMenu = (TextView)findViewById(R.id.order_history_list);
        tabStoreInfo = (TextView)findViewById(R.id.calc_history_list);

        sessionManager = new SessionManager(StoreInfoReNewer.this, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> hashMap = sessionManager.getUsersDetailFromSession();
        _phone = hashMap.get(SessionManager.KEY_PHONENUMBER);
        myIntent = getIntent();
        storedIdStr=myIntent.getStringExtra("store_id");
        saveFavoriteOnce();
//        if (_phone.equals("")) {
//            topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_empty);
//        }else {
//            checkFavorite();
//        }
        setTabEvent();
//        if (_phone.equals("")) {
//            topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_empty);
//        }else {
//            checkFavorite();
//        }
        makeRequestGetStore(Integer.parseInt(storedIdStr));


    }

    private void saveFavoriteOnce() {
        sp = getSharedPreferences("saveStoreId", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("store_id", storedIdStr);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setOnClickFavorite();
        getFavoriteStoreId();
        checkFavorite();

    }
    @Override
    protected void onPause() {
        super.onPause();
//        overridePendingTransition(0, 0);
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }
    public void setOnClickFavorite() {
        heartClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!BaroUtil.loginCheck(StoreInfoReNewer.this)) {
                    return;
                }
                if(result) {
                    Log.i("result", "true");
                    //등록 - > 미등록
                    String phone = _phone;
                    String storeId = storedIdStr;

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("phone", phone);
                    hashMap.put("store_id", storeId);
                    String url = "http://3.35.180.57:8080/FavoriteDelete.do";

                    makeRequestFavorteRem(url, hashMap);
                    //mFavorite.setImageResource(R.drawable.heart_empty);
                    topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_empty);
                    result = false;
                    DeleteFavoriteDialog deleteFavoriteDialog = new DeleteFavoriteDialog(StoreInfoReNewer.this);
                    deleteFavoriteDialog.callFunction();
                    //true 등록되어있을때
                }
                else {
                    Log.i("result", "false");
                    //미등록 -> 등록
                    String phone = _phone;
                    String storeId = storedIdStr;

                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put("phone", phone);
                    hashMap.put("store_id", storeId);

                    String url = "http://3.35.180.57:8080/FavoriteSave.do";
                    makeRequestFavoriteReg(url, hashMap);

                    //mFavorite.setImageResource(R.drawable.heart_full);
                    topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_fill);
                    result = true;
                    AddFavoriteDialog addFavoriteDialog = new AddFavoriteDialog(StoreInfoReNewer.this);
                    addFavoriteDialog.callFunction();
                    //false 등록되어있지 않을 때
                }
            }
        };
    }
    private void getFavoriteStoreId() {
        sp = getSharedPreferences("saveStoreId", MODE_PRIVATE);
        if(sp == null) {
            return;
        }
        else {
            if (sp.getString("store_id", "") == "") {

            }else {
                storedIdStr = sp.getString("store_id", "");
            }
        }
    }
    public HashMap setHashDataForCheckFavorite() {
        HashMap<String, Object> data = new HashMap<>();
        data.put("phone", _phone);
        data.put("store_id", storedIdStr);
        Log.i("storeID", storedIdStr);

        return data;
    }
    private void checkFavorite() {
        makeRequestForCheckFavorite(setHashDataForCheckFavorite());
    }
    private void makeRequestForCheckFavorite(HashMap data) {
        Log.e("comein?", "aaaa");
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("FavoriteExist.do");
        RequestQueue requestQueue = Volley.newRequestQueue(StoreInfoReNewer.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("checkFavorite", response.toString());
                parsing(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void parsing(String toString) {
        try {
            JSONObject jsonObject = new JSONObject(toString);
            result = jsonObject.getBoolean("result");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        if(result) {
            topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_fill);
        }
        else {
            topBar.setEtcImageWhereUsedStoreInfo(R.drawable.favorite_empty);
        }
    }
    private void makeRequestFavorteRem(String url, HashMap<String, String> data) {
        RequestQueue requestQueue = Volley.newRequestQueue(StoreInfoReNewer.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("removeFAV", response.toString());
                        jsonParsing(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("favremoveerr", "err");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void makeRequestFavoriteReg(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(StoreInfoReNewer.this);
        Log.e("url", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("responseeee", response.toString());
                        jsonParsing(response);
                        try {
                            if (response.getBoolean("result")) {
                                Log.e("favreg", "등록성공");
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("favoriteRegerror", "err");
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }
    public void jsonParsing(JSONObject result){
        Boolean _result = false;
        String _message = null;
        try{
            _result = result.getBoolean("result");
            _message = result.getString("message");
        }
        catch(JSONException e){
            e.printStackTrace();
        }
    }
    public void makeRequestGetStore(final int number) {
        String url = new UrlMaker().UrlMake("StoreFindById.do?store_id="+ number);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsingStore(response);
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

    private void jsonParsingStore(String response) {
        Log.i("JSONPARSING", response);
        Gson gson = new GsonBuilder().create();
        storeDetailData = gson.fromJson(response, StoreDetail.class);
        setTitleName();
    }

    private void setTabEvent() {
        final FragmentManager fm = getSupportFragmentManager();
        storeMenuFragment = (StoreMenuFragment) fm.findFragmentById(R.id.store_list_fragment);
        storeDetailInfoFragment = (StoreDetailInfoFragment) fm.findFragmentById(R.id.store_detail_info_fragment);
        fm.beginTransaction().show(storeMenuFragment).commit();
        fm.beginTransaction().hide(storeDetailInfoFragment).commit();

        TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabs.getChildAt(0)).getChildAt(0)).getChildAt(1));
        tv.setTextColor(getResources().getColor(R.color.main));
        Log.i("tabsTabItem", tv.getText().toString());
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabs.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                tv.setTextColor(getResources().getColor(R.color.main));
                switch (tab.getPosition()) {
                    case 0 :
                        tabs.getChildAt(0);
                        fm.beginTransaction().show(storeMenuFragment).commit();
                        fm.beginTransaction().hide(storeDetailInfoFragment).commit();
                        break;
                    case 1 :
                        fm.beginTransaction().show(storeDetailInfoFragment).commit();
                        fm.beginTransaction().hide(storeMenuFragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabs.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                tv.setTextColor(getResources().getColor(R.color.text_info_color));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView tv = (TextView)(((LinearLayout)((LinearLayout)tabs.getChildAt(0)).getChildAt(tab.getPosition())).getChildAt(1));
                tv.setTextColor(getResources().getColor(R.color.main));
            }
        });
    }

    private void setTitleName() {
        topBar.setTitleStringWhereUsedEventsAndListStore(storeDetailData.getStore_name());
    }

    @Override
    public void onBack() {
        onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }

    @Override
    public void clickImage() {
        heartClickListener.onClick(null);
    }
}