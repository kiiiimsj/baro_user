package com.example.wantchu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.AdvertiseAdapter;
import com.example.wantchu.Adapter.NewStoreListAdapter;
import com.example.wantchu.Adapter.TypeAdapter;
import com.example.wantchu.Adapter.UltraStoreListAdapter;
import com.example.wantchu.AdapterHelper.TypeHelperClass;
import com.example.wantchu.Database.StoreSessionManager;
import com.example.wantchu.Dialogs.AppStartAdDialog;
import com.example.wantchu.Dialogs.SearchDialog;
import com.example.wantchu.JsonParsingHelper.EventHelperClass;
import com.example.wantchu.JsonParsingHelper.TypeListParsing;
import com.example.wantchu.JsonParsingHelper.TypeParsing;
import com.example.wantchu.JsonParsingHelper.ViewPagersListStoreParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.OrderCancelIfNotAccept;
import com.example.wantchu.helperClass.ViewPagerCustomDuration;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, TypeAdapter.OnListItemLongSelectedInterface, TypeAdapter.OnListItemSelectedInterface, UltraStoreListAdapter.OnListItemLongSelectedInterface, UltraStoreListAdapter.OnListItemSelectedInterface, NewStoreListAdapter.OnListItemSelectedInterface, NewStoreListAdapter.OnListItemLongSelectedInterface {

    private static String TAG = "MainPage";
    RecyclerView mRecyclerView;
    private Intent serviceIntent;
    //울트라store recycler
    RecyclerView ultraStoreRecyclerView;
    ViewPagersListStoreParsing listStoreParsing;
    UltraStoreListAdapter ultraAdapter;
    //New Store recycler
    RecyclerView newStoreRecyclerView;
    ViewPagersListStoreParsing newListStoreParsing;
    NewStoreListAdapter newStoreListAdapter;

    RecyclerView.Adapter adapter;

    Context context;

    TextView eventCountSet;
    ViewPagerCustomDuration viewPager;
    int currentPos;
    AdvertiseAdapter advertiseAdapter;

    SharedPreferences sp;
    Gson gson;


    StoreSessionManager storeSessionManager;
    ProgressApplication progressApplication;
    EventHelperClass eventHelperClass;
    ViewPager.OnPageChangeListener changeListener;

    TextView mAddress;
    ImageView mMapButton;

    LatLng latLng;
    myGPSListener myGPSListener;
    ///////// 태영
    ImageView call_search;
    /////////
    void goChangSin(){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://celebe.ohmyapp.io/bnb/aggregateForTable",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG+"SUCCESS",response);
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        sp = getSharedPreferences("favorite", MODE_PRIVATE);
        gson = new GsonBuilder().create();
        mRecyclerView = findViewById(R.id.recyclerView);

        ultraStoreRecyclerView = findViewById(R.id.ultra_store);
        newStoreRecyclerView = findViewById(R.id.new_store);

        viewPager = findViewById(R.id.info_image);
//        goChangSin();
        eventCountSet = findViewById(R.id.event_count);
        AppStartAdDialog appStartAdDialog = new AppStartAdDialog(MainPage.this);
        appStartAdDialog.callFunction();
        context = this;
        mAddress = findViewById(R.id.address);
        mMapButton = findViewById(R.id.map_button);
        // 왼쪽 사이드바
        storeSessionManager = new StoreSessionManager(MainPage.this, StoreSessionManager.STORE_SESSION);
        storeSessionManager.setIsFavorite(false);
        ///////// 태영
        call_search = findViewById(R.id.search_dialog);
        /////////
        startLocation();
//        if (OrderCancelIfNotAccept.serviceIntent==null) {
//            serviceIntent = new Intent(this, OrderCancelIfNotAccept.class);
//            startService(serviceIntent);
//        } else {
//            serviceIntent = OrderCancelIfNotAccept.serviceIntent;//getInstance().getApplication();
//            Toast.makeText(getApplicationContext(), "already", Toast.LENGTH_LONG).show();
//        }

        makeRequestForEventThread();
        // 타입 버튼 동적으로 만드는 메소드
        makeRequest();

        myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(null);
        if(latLng == null) {
            mAddress.setText("GPS를 설정 해 주세요");
        }



        makeRequestUltraStore(setHashMapData());
        makeRequestNewStore(setHashMapData());

        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MyMap.class);
                Intent intent = new Intent(getApplicationContext(), NewMyMap.class);
                startActivity(intent);
            }
        });
        mMapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, MyMap.class);
                startActivity(intent);
            }
        });

        ///////// 태영
        call_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchDialog searchDialog = new SearchDialog(MainPage.this);
                searchDialog.callFunction();
            }
        });

        progressApplication.progressOFF();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG,"RESUME");
        latLng = myGPSListener.startLocationService(mAddress);
        if(latLng == null) {
            mAddress.setText("GPS를 설정 해 주세요");
        }
        makeRequestUltraStore(setHashMapData());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceIntent!=null) {
            stopService(serviceIntent);
            serviceIntent = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    private void startLocation() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("설정");
            builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
            builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private void setEventCountSet(int position) {
        eventCountSet.setText((position % eventHelperClass.event.size() + 1)+"  /  "+eventHelperClass.event.size());
    }
    public HashMap setHashMapData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("latitude",latLng.latitude+"");
        data.put("longitude",latLng.longitude+"");
        return data;
    }
    public void makeRequestUltraStore(HashMap data) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreFindByUltra.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("StoreUltra", response.toString());
                        jsonParsingUltraStore(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    public void makeRequest() {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "TypeFindAll.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.i("type", "request made to " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("type", response);
                        mRecyclerView(response);
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

    public void makeRequestNewStore(HashMap data) {
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreFindByNew.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("StoreUltra", response.toString());
                        jsonParsingNewStore(response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void jsonParsingNewStore(String toString) {
        Gson gson = new Gson();
        newListStoreParsing=gson.fromJson(toString, ViewPagersListStoreParsing.class);
        setNewListStoreRecyclerView();
    }

    private void setNewListStoreRecyclerView() {
        newStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        newStoreListAdapter = new NewStoreListAdapter(newListStoreParsing, this, this, this);
        newStoreRecyclerView.setAdapter(newStoreListAdapter);
    }

    private void mRecyclerView(String result) {
        ArrayList<TypeHelperClass> DataList = new ArrayList<>();
        HashMap<String, TypeHelperClass> hashMap = new HashMap<>();
        hashMap.put("TypeHelperClass", new TypeHelperClass("", "", ""));
        TypeParsing typeParsing = new TypeParsing();
        jsonParsing(result, typeParsing);
        adapter = new TypeAdapter(DataList, this, this, MainPage.this);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        for(int i = 0; i < typeParsing.getTypeList().size();i++){
            TypeListParsing typeListParsing = typeParsing.getTypeList().get(i);
            TypeHelperClass typeHelperClass = new TypeHelperClass(typeListParsing.getTypeName(), typeListParsing.getTypeCode(), typeListParsing.getTypeImage());
            typeHelperClass.typeNames.add(typeListParsing.getTypeName());
            typeHelperClass.typeCodes.add(typeListParsing.getTypeCode());
            DataList.add(typeHelperClass);
        }
        mRecyclerView.setAdapter(adapter);
    }

    //ultrastore 가져오기 위한 recyclerview
    private void ultraStoreRecyclerView() {
        ultraStoreRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        ultraAdapter = new UltraStoreListAdapter(listStoreParsing, this, this, MainPage.this);
        ultraStoreRecyclerView.setAdapter(ultraAdapter);
    }

    private void jsonParsingUltraStore(String result){
        Gson gson = new Gson();
        listStoreParsing = gson.fromJson(result, ViewPagersListStoreParsing.class);
        ultraStoreRecyclerView();
    }

    //왼쪽 사이드바 메뉴 클릭시
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.left_coupon:
                startActivity(new Intent(MainPage.this, SideMyCoupon.class));
                break;
            case R.id.left_noti:
                Intent intent = new Intent(MainPage.this, Notice.class);
                intent.putExtra("type", "NOTICE");
                startActivity(intent);
                break;
            case R.id.left_entry_request:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://pf.kakao.com/_bYeuK/chat")));
                break;
            case R.id.left_one_to_one:

                break;
            case R.id.left_events:
                startActivity(new Intent(MainPage.this, Alerts.class));
                break;
        }
        return true;
    }




    private synchronized void jsonParsing(String result, TypeParsing typeParsing){
        try {
            Boolean result2 = (Boolean) new JSONObject(result).getBoolean("result");
            typeParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("type");
            ArrayList<TypeListParsing> typeListParsings = new ArrayList<TypeListParsing>();
            for(int i = 0; i<jsonArray.length();i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                String type_name = jObject.optString("type_name");
                String type_code = jObject.optString("type_code");
                String type_image = jObject.optString("type_image");
                TypeListParsing typeListParsing = new TypeListParsing(type_name, type_code, type_image);
                typeListParsings.add(typeListParsing);
            }
            typeParsing.setTypeList(typeListParsings);
            Log.i("MainPage", result2.toString());
        }
        catch(JSONException e){
            e.printStackTrace();
        }

    }
    public void makeRequestForEventThread() {
        String url = new UrlMaker().UrlMake("EventFindAll.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("GET_EVENT_PICTURE", response);
                eventParsing(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void setAdvertiseAdapter() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                currentPos = position;
            }
            @Override
            public void onPageSelected(int position) {
                Log.i("onPageSelected", position+"");
                setEventCountSet(position);
                currentPos = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
//                if(state == ViewPager.SCROLL_STATE_DRAGGING) {
//                    if(currentPos+1 == viewPager.getChildCount()) {
//                        viewPager.setCurrentItem(0);
//                        setEventCountSet(0);
//                        currentPos = 0;
//                    }
//                }
            }
        });
    }
    private void eventParsing(String response) {
        Gson gson = new Gson();
        eventHelperClass = gson.fromJson(response, EventHelperClass.class);
        advertiseAdapter = new AdvertiseAdapter(context, eventHelperClass);
        viewPager.setAdapter(advertiseAdapter);
        viewPager.setCurrentItem(eventHelperClass.event.size() * 1000);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setScrollDurationFactor(3);
        setEventCountSet(0);

        setAdvertiseAdapter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewPager.setCurrentItem((currentPos + 1),true);
                        }
                    });
                }

            }
        }).start();
    }

    @Override
    public void onItemLongSelected(View v, int adapterPosition) {
    }

    @Override
    public void onItemSelected(View v, int position) {
        TypeAdapter.TypeViewHolder viewHolder = (TypeAdapter.TypeViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, ListStorePage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("type_code", viewHolder.code.getText().toString());
        intent.putExtra("type_name", viewHolder.title.getText().toString());
        intent.putExtra("list_type", "find_type");
        startActivity(intent);
    }

    @Override
    public void onNewStoreItemSelected(View v, int position) {
        NewStoreListAdapter.NewStoreViewHolder viewHolder= (NewStoreListAdapter.NewStoreViewHolder) newStoreRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, StoreInfoReNewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onLongNewStoreItemSelected(View v, int adapterPosition) {

    }


    @Override
    public void onUltraStoreLongSelected(View v, int adapterPosition) {

    }

    @Override
    public void onUltraStoreSelected(View v, int position) {
        UltraStoreListAdapter.UltraStoreListViewHolder viewHolder= (UltraStoreListAdapter.UltraStoreListViewHolder) ultraStoreRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, StoreInfoReNewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        Log.e("ultraid", viewHolder.storeId.getText().toString());
        startActivity(intent);
    }
}
