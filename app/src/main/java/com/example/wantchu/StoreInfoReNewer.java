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
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.JsonParsingHelper.FavoriteListParsing;
import com.example.wantchu.JsonParsingHelper.FavoriteParsing;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreInfoReNewer extends AppCompatActivity {
    ImageView mFavorite;
    TextView titleName;
    SessionManager sessionManager;
    String _phone;
    String storedIdStr;
    TabLayout tabs;
    boolean result =false;
    SharedPreferences sp;
    StoreMenuFragment storeMenuFragment;
    StoreDetailInfoFragment storeDetailInfoFragment;
    StoreDetail storeDetailData;
    Intent myIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_info_re_newer);

        mFavorite = findViewById(R.id.favorite);
        titleName = findViewById(R.id.type_name);
        tabs = findViewById(R.id.tab_tabs);

        sessionManager = new SessionManager(getApplicationContext(), SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> hashMap = sessionManager.getUsersDetailFromSession();
        _phone = hashMap.get(SessionManager.KEY_PHONENUMBER);
        myIntent = getIntent();
        storedIdStr=myIntent.getStringExtra("store_id");
        checkFavorite();
        setTabEvent();
        makeRequestGetStore(Integer.parseInt(storedIdStr));
    }
    @Override
    protected void onResume() {
        super.onResume();
        getFavoriteStoreId();
        checkFavorite();
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }

    public void onClickFavorite(View view) {
        if(result) {
            //등록 - > 미등록
            String phone = _phone;
            String storeId = storedIdStr;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("phone", phone);
            hashMap.put("store_id", storeId);
            String url = "http://15.165.22.64:8080/FavoriteDelete.do";

            makeRequestFavorteRem(url, hashMap);
            mFavorite.setImageResource(R.drawable.heart_empty);

            DeleteFavoriteDialog deleteFavoriteDialog = new DeleteFavoriteDialog(getApplicationContext());
            deleteFavoriteDialog.callFunction();
            //true 등록되어있을때
        }
        else {
            //미등록 -> 등록
            String phone = _phone;
            String storeId = storedIdStr;

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("phone", phone);
            hashMap.put("store_id", storeId);

            String url = "http://15.165.22.64:8080/FavoriteSave.do";
            makeRequestFavoriteReg(url, hashMap);

            mFavorite.setImageResource(R.drawable.heart_full);
            AddFavoriteDialog addFavoriteDialog = new AddFavoriteDialog(getApplicationContext());
            addFavoriteDialog.callFunction();
            //false 등록되어있지 않을 때
        }
    }
    private void getFavoriteStoreId() {
        sp =getSharedPreferences("favorite", Context.MODE_PRIVATE);
        if(sp == null) {
            return;
        }
        Gson gson = new GsonBuilder().create();
        String contactFavorite = sp.getString("favorite","");
        Log.i("FAVORITE", contactFavorite);
        if(!contactFavorite.equals("")){
            FavoriteParsing favoriteParsing = gson.fromJson(contactFavorite, FavoriteParsing.class);
            ArrayList<FavoriteListParsing> favoriteList = favoriteParsing.getFavorite();
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
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("FavoriteExist.do");
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
            mFavorite.setImageResource(R.drawable.heart_full);
        }
        else {
            mFavorite.setImageResource(R.drawable.heart_empty);
        }
    }
    private synchronized void makeRequestFavorteRem(String url, HashMap<String, String> data) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url, new JSONObject(data),
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

    private synchronized void makeRequestFavoriteReg(String url, HashMap data) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
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
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0 :
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

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setTitleName() {
        titleName.setText(storeDetailData.getStore_name());
    }
}