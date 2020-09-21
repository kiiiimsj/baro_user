package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.ListStoreAdapter;
import com.example.wantchu.AdapterHelper.ListStoreHelperClass;
import com.example.wantchu.Database.StoreSessionManager;
import com.example.wantchu.JsonParsingHelper.ListStoreListParsing;
import com.example.wantchu.JsonParsingHelper.ListStoreParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

//import com.example.wantchu.Database.SendToServer;

public class ListStorePage extends AppCompatActivity implements ListStoreAdapter.OnListItemLongSelectedInterface, ListStoreAdapter.OnListItemSelectedInterface , AutoPermissionsListener {
    private final static int FIRST = 1;
    private final static int AFTER_FIRST =2;
    private final static int ON_RESTART = 3;
    ImageView backButton;

    SwipyRefreshLayout refreshLayout;
    int currentPos;
    RecyclerView mRecyclerView;
    ListStoreAdapter adapter;

    TextView typeName;
    //메인페이지에서 넘어온 리스트에 필요한 값들
    String type_code;
    String type_name;

    //검색을 통해서 넘어온 리스트에 필요한 값
    String storeSearch;

    LatLng latLng;

    StoreSessionManager storeSessionManager;
    ArrayList<ListStoreListParsing> listStoreListParsings;
    ProgressApplication progressApplication;
    SharedPreferences saveListSet;

    ListStoreParsing listStoreParsing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CREATE_LISTSTORE", 1+"");
        setContentView(R.layout.activity_list_store_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        refreshLayout = findViewById(R.id.refresh_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.back_pressed);
        typeName = findViewById(R.id.type_name);
        saveListSet = getSharedPreferences("saveList", MODE_PRIVATE);
        storeSessionManager = new StoreSessionManager(getApplicationContext(), StoreSessionManager.STORE_SESSION);
        currentPos = 0;
        myGPSListener myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(null);
        chooseShowList(FIRST);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                currentPos += 20;
                makeRequestForTypeFind(setHashDataForTypeFind(), AFTER_FIRST);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("onPause", "onPause");
        SharedPreferences sf =getSharedPreferences("onPause", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        editor.putBoolean("onPause", true);
        editor.apply();
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sf =getSharedPreferences("onPause", MODE_PRIVATE);
        if(sf.getBoolean("onPause", false)){
            Log.i("isPause", "11");
            chooseShowList(ON_RESTART);
            SharedPreferences.Editor editor = sf.edit();
            editor.clear();
            editor.apply();
            editor.commit();
        }
    }
    public void chooseShowList(int state) {
        Intent intent = getIntent();
        if(intent != null) {
            saveListSet.edit().putString("list_type", intent.getStringExtra("list_type"));
            saveListSet.edit().apply();
            saveListSet.edit().commit();
        }
        if(intent.getStringExtra("list_type")==null) {
            String value = saveListSet.getString("list_type", null);
            if(value == null) {
                Toast.makeText(this, "로딩 실패", Toast.LENGTH_LONG).show();
                progressApplication.progressOFF();
                return;
            }
            intent.putExtra("list_type", value);
        }
        Log.i("intent.getString", intent.getStringExtra("list_type"));
        if(intent.getStringExtra("list_type").equals("search")){
            storeSearch = intent.getStringExtra("searchStore");
            Log.i("storeSearchValue", storeSearch);
            typeName.setText("검색 가게");
            makeRequestForSearch(urlMaker(storeSearch));
            return;
        }
        if(intent.getStringExtra("list_type").equals("find_type")) {
            type_code = intent.getStringExtra("type_code");
            type_name = intent.getStringExtra("type_name");
            Log.i("TYPE_CODE", 1+"");
            typeName.setText(type_name);
            makeRequestForTypeFind(setHashDataForTypeFind(), state);
        }
    }

    public String urlMaker(String keyword) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        StringBuilder urlBuilder = new StringBuilder(url);
        urlBuilder.append("StoreSearch.do?keyword=");
        urlBuilder.append(keyword);
        return urlBuilder.toString();
    }
    public HashMap setHashDataForTypeFind() {
        SharedPreferences getStore = getSharedPreferences("storeID", MODE_PRIVATE);
        if(getStore.getInt("current", -1) != -1) {
            currentPos = getStore.getInt("current", -1);
        }
        HashMap<String, Object> data = new HashMap<>();
        data.put("type_code", type_code);
        data.put("startPoint", currentPos);
        data.put("latitude",latLng.latitude);
        data.put("longitude",latLng.longitude);

        return data;
    }
    private void makeRequestForTypeFind(HashMap data, final int state) {
//        JSONObject jsonObject = new JSONObject();
//        Log.e("type_code",type_code);
//        try {
//            jsonObject.put("type_code",type_code);
//            jsonObject.put("latitude",latLng.latitude);
//            jsonObject.put("longitude",latLng.longitude);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

        String lastUrl = "StoreInfoFindByType.do?";
//        String lastUrl = "StoreInfoFindByType.do?type_code=" + type_code;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.i("storesList", "request made to " + url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("storeList", response.toString());
                        jsonParsing(response.toString(), state);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("storeList", "error");
                    }
                });
        requestQueue.add(request);
    }

    private void makeRequestForSearch(String url){
        //String url = "http://54.180.56.44:8080/StoreSearch.do?keyword="+storeSearch;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("searchStore", url); //ok
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("searchStore", response);
                        //jsonParsing(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("searchStoreError", "error");
                    }
                });
        requestQueue.add(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }

    @Override
    public void onDenied(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions denied : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGranted(int requestCode, String[] permissions) {
        Toast.makeText(this, "permissions granted : " + permissions.length, Toast.LENGTH_LONG).show();
    }

    private void mRecyclerView(){
        mRecyclerView.setHasFixedSize(true);
        ArrayList<ListStoreHelperClass> DataList = new ArrayList<>();
        ArrayList<ListStoreHelperClass> DataListForIsOpen = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        int [] storeIds = new int[listStoreParsing.store.size()];
        for(int i = 0; i< listStoreParsing.store.size();i++){

            ListStoreHelperClass listStoreHelperClass = new ListStoreHelperClass(listStoreParsing.store.get(i).getStore_name(), listStoreParsing.store.get(i).getStore_location(), listStoreParsing.store.get(i).getStore_image(), listStoreParsing.store.get(i).getDistance(), listStoreParsing.store.get(i).getStore_id(), listStoreParsing.store.get(i).getIs_open());

            Log.e("hey", listStoreParsing.store.get(i).getStore_image());

            listStoreHelperClass.storeNames.add(listStoreParsing.store.get(i).getStore_name());
            listStoreHelperClass.storeLocations.add(listStoreParsing.store.get(i).getStore_location());
            listStoreHelperClass.storeImages.add(listStoreParsing.store.get(i).getStore_image());
            listStoreHelperClass.storeDistances.add(listStoreParsing.store.get(i).getDistance());
            listStoreHelperClass.storeIds.add(listStoreParsing.store.get(i).getStore_id());
            storeIds[i] = listStoreParsing.store.get(i).getStore_id();

            listStoreHelperClass.storesIsOpen.add(listStoreParsing.store.get(i).getIs_open());
            if(listStoreParsing.store.get(i).getIs_open().equals("Y")) {
                DataListForIsOpen.add(listStoreHelperClass);
                continue;
            }
            DataList.add(listStoreHelperClass);
        }
        DataListForIsOpen.addAll(DataList);
        SharedPreferences getStoreId = getSharedPreferences("storeID", MODE_PRIVATE);
        getStoreId.edit().putString("listStore", listStoreParsing.toString()).apply();
        getStoreId.edit().putInt("currentPos", currentPos);
        getStoreId.edit().apply();
        getStoreId.edit().commit();

        adapter = new ListStoreAdapter(DataListForIsOpen, this, this,  this);
        mRecyclerView.setAdapter(adapter);
        progressApplication.progressOFF();
        refreshLayout.setRefreshing(false);
    }

    private void jsonParsing(String result, int state) {
        if(state == FIRST) {
            Gson gson = new Gson();
            listStoreParsing = gson.fromJson(result, ListStoreParsing.class);
            mRecyclerView();
        }
        if(state ==AFTER_FIRST) {
            Gson gson = new Gson();
            ListStoreParsing getAddList = gson.fromJson(result, ListStoreParsing.class);
            if(!getAddList.isResult()) {
                Toast.makeText(this, "더 불러올 내역이 없습니다", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
            else {
                listStoreParsing.store.addAll(getAddList.store);
                mRecyclerView();
            }
        }
        if(state == ON_RESTART) {
            if(listStoreParsing == null) {
                SharedPreferences getStore = getSharedPreferences("storeID", MODE_PRIVATE);
                Gson gson = new Gson();
                listStoreParsing = gson.fromJson(getStore.getString("listStore", null), ListStoreParsing.class);
                mRecyclerView();
            }
        }
        mRecyclerView();
    }

    private JSONArray jsonParsingType(String result) {
        ListStoreParsing listStoreParsing = new ListStoreParsing();
        boolean result2 = false;
        JSONArray jsonArray = null;
        try {
            result2 = (Boolean) new JSONObject(result).getBoolean("result");
            listStoreParsing.setMessage(new JSONObject(result).getString("message"));
            jsonArray = new JSONObject(result).getJSONArray("store");
            if (result2 == false|| jsonArray == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ListStorePage.this, "가게정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    @Override
    public void onItemLongSelected(View v, int adapterPosition) {
        ListStoreAdapter.ListStoreViewHolder listStoreViewHolder = (ListStoreAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(adapterPosition);
        Intent intent = new Intent(getApplicationContext(), StoreInfoReNewer.class);
        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        intent.putExtra("store_name", listStoreViewHolder.storeName.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(View v, int position) {
        ListStoreAdapter.ListStoreViewHolder listStoreViewHolder = (ListStoreAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(getApplicationContext(), StoreInfoReNewer.class);
        if(listStoreViewHolder.storeId == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ListStorePage.this, "해당 가게가 존재하지 않습니다 재접속 해주세요", Toast.LENGTH_LONG);
                }
            });
        }
        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        startActivity(intent);
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }

    private double getDistance(Location myLocation, Location storeLocation){
        double distance;
        distance = myLocation.distanceTo(storeLocation);
        return distance;
    }
}