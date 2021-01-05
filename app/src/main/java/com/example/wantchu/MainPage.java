package com.example.wantchu;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
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
import com.example.wantchu.Fragment.AlarmBell;
import com.example.wantchu.JsonParsingHelper.EventHelperClass;
import com.example.wantchu.JsonParsingHelper.TypeListParsing;
import com.example.wantchu.JsonParsingHelper.TypeParsing;
import com.example.wantchu.JsonParsingHelper.ViewPagersListStoreParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.BaroUtil;
import com.example.wantchu.helperClass.OrderCancelIfNotAccept;
import com.example.wantchu.helperClass.ViewPagerCustomDuration;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import maes.tech.intentanim.CustomIntent;

import static com.example.wantchu.helperClass.BaroUtil.checkGPS;

public class MainPage extends AppCompatActivity implements TypeAdapter.OnListItemLongSelectedInterface, TypeAdapter.OnListItemSelectedInterface, UltraStoreListAdapter.OnListItemLongSelectedInterface, UltraStoreListAdapter.OnListItemSelectedInterface,
        NewStoreListAdapter.OnListItemSelectedInterface, NewStoreListAdapter.OnListItemLongSelectedInterface, AutoPermissionsListener, ActivityCompat.OnRequestPermissionsResultCallback {
    final private String TAG = this.getClass().getSimpleName();
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
    SwipeRefreshLayout refreshLayout;

    /////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AutoPermissions.Companion.loadAllPermissions(this, 101);

        setContentView(R.layout.activity_main_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        sp = getSharedPreferences("favorite", MODE_PRIVATE);
        gson = new GsonBuilder().create();
        mRecyclerView = findViewById(R.id.recyclerView);
        ultraStoreRecyclerView = findViewById(R.id.ultra_store);
        newStoreRecyclerView = findViewById(R.id.new_store);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            stringBuilder.append(i).append("/");
        }
        Log.i("stringBuilderBefore", stringBuilder.toString());
        stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("/"));
        Log.i("stringBuilderAfter", stringBuilder.toString());

        viewPager = findViewById(R.id.info_image);
        eventCountSet = findViewById(R.id.event_count);
        AppStartAdDialog appStartAdDialog = new AppStartAdDialog(MainPage.this);
        appStartAdDialog.callFunction();
        context = this;
        mAddress = findViewById(R.id.address);
        mMapButton = findViewById(R.id.map_button);
        storeSessionManager = new StoreSessionManager(MainPage.this, StoreSessionManager.STORE_SESSION);
        storeSessionManager.setIsFavorite(false);
        ///////// 태영
        call_search = findViewById(R.id.search_dialog);
        refreshLayout = findViewById(R.id.refreshLayout);

        makeRequestForEventThread();
        makeRequest();

        myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(mAddress);
        if(!BaroUtil.checkGPS(this)) {
            startLocation();
        }

        else {
            makeRequestUltraStore(setHashMapData());
            makeRequestNewStore(setHashMapData());
        }
        mAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MyMap.class);
                Intent intent = new Intent(getApplicationContext(), NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(MainPage.this,"right-to-left");
            }
        });
        mMapButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, NewMyMap.class);
                startActivity(intent);
                CustomIntent.customType(MainPage.this,"right-to-left");
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(TAG,TAG);
                latLng = myGPSListener.startLocationService(mAddress);
                makeRequestUltraStore(setHashMapData());
                makeRequestNewStore(setHashMapData());
                refreshLayout.setRefreshing(false);
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
        Log.e("onResume", 1+"");
        latLng = myGPSListener.startLocationService(mAddress);
        Log.e("RALO", String.valueOf(BaroUtil.checkGPS(this)));
        if(BaroUtil.checkGPS(this)) {
            makeRequestUltraStore(setHashMapData());
            makeRequestNewStore(setHashMapData());
        }
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
    }

    private void startLocation() {
        Log.e("startLocation", "start");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("설정");
        builder.setCancelable(true);
        builder.setMessage("어플을 사용하기위해선 위치서비스를 켜주세요");
        builder.setPositiveButton("예", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int i) {
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();
        //Overriding the handler immediately after show is probably a better approach than OnShowListener as described below
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                dialog.dismiss();
            }
        });
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
            }
        });
    }
    private void eventParsing(String response) {
        Gson gson = new Gson();
        eventHelperClass = gson.fromJson(response, EventHelperClass.class);
        advertiseAdapter = new AdvertiseAdapter(context, eventHelperClass);
        viewPager.setAdapter(advertiseAdapter);
        viewPager.setCurrentItem(eventHelperClass.event.size() * 500);
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
        Intent intent = new Intent(this, ListStorePage.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//        int i = 0;
        intent.putExtra("type_code", viewHolder.code.getText().toString());
        intent.putExtra("type_name", viewHolder.title.getText().toString());
        intent.putExtra("list_type", "find_type");
        startActivity(intent);
        CustomIntent.customType(this,"left-to-right");
//        startActivity(new Intent(this, ListStorePage.class));
//        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
//
//        overridePendingTransition(R.anim.anim_slide_out_right, R.anim.anim_slide_in_left);

    }

    @Override
    public void onNewStoreItemSelected(View v, int position) {
        NewStoreListAdapter.NewStoreViewHolder viewHolder= (NewStoreListAdapter.NewStoreViewHolder) newStoreRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(MainPage.this, StoreInfoReNewer.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        startActivity(intent);
        CustomIntent.customType(this,"left-to-right");
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
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", viewHolder.storeId.getText().toString());
        Log.e("ultraid", viewHolder.storeId.getText().toString());
        startActivity(intent);
        CustomIntent.customType(this,"left-to-right");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        finishAndRemoveTask();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    @Override
    public void onDenied(int i, @NotNull String[] strings) {

    }

    @Override
    public void onGranted(int i, @NotNull String[] strings) {

    }
}
