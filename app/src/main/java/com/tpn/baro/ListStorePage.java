package com.tpn.baro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.StoreListAdapter;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.JsonParsingHelper.ListStoreParsing;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.BaroUtil;
import com.tpn.baro.helperClass.MyGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayout;
import com.orangegangsters.github.swipyrefreshlayout.library.SwipyRefreshLayoutDirection;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;
import com.tpn.baro.helperClass.ProgressApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import maes.tech.intentanim.CustomIntent;

//import com.example.baro.Database.SendToServer;

public class ListStorePage extends AppCompatActivity implements StoreListAdapter.OnListItemSelectedInterface , AutoPermissionsListener, TopBar.OnBackPressedInParentActivity {
    private final static int FIRST = 1;
    private final static int AFTER_FIRST =2;
    private final static int ON_RESTART = 3;
    ImageView backButton;

    SwipyRefreshLayout refreshLayout;
    int currentPos;
    RecyclerView mRecyclerView;
    StoreListAdapter adapter;
    public static boolean onPause = false;

    TextView typeName;
    //메인페이지에서 넘어온 리스트에 필요한 값들
    String type_code;
    String type_name;

    //검색을 통해서 넘어온 리스트에 필요한 값
    String storeSearch;

    LatLng latLng;

    ProgressApplication progressApplication;
    SharedPreferences saveListSet;

    ListStoreParsing listStoreParsing;


    FragmentManager fm;
    TopBar topbar;
    static int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onPause = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BaroUtil.setStatusBarColor(ListStorePage.this, this.toString());
        }
        setContentView(R.layout.activity_list_store_page);

        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        count = 1;
        refreshLayout = findViewById(R.id.refresh_list);
        mRecyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.back_pressed);
        saveListSet = getSharedPreferences("saveList", MODE_PRIVATE);
        currentPos = 0;
        fm = getSupportFragmentManager();
        topbar = (TopBar) fm.findFragmentById(R.id.top_bar);

        MyGPSListener myGPSListener = new MyGPSListener(this);
        latLng = myGPSListener.startLocationService(null);
        chooseShowList(FIRST);
        refreshLayout.setOnRefreshListener(new SwipyRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(SwipyRefreshLayoutDirection direction) {
                currentPos += 20;
                makeRequestForTypeFind(setHashDataForTypeFind(), AFTER_FIRST);
            }
        });
        fm.beginTransaction().show(topbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(count == 0){
            chooseShowList(ON_RESTART);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        onPause= true;
//        overridePendingTransition(0, 0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        onPause = false;
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
        if(intent.getStringExtra("list_type").equals("search")){
            storeSearch = intent.getStringExtra("searchStore");
            topbar.setTitleStringWhereUsedEventsAndListStore("검색 가게");
            makeRequestForSearch(state);
            return;
        }
        if(intent.getStringExtra("list_type").equals("find_type")) {
            type_code = intent.getStringExtra("type_code");
            type_name = intent.getStringExtra("type_name");
            topbar.setTitleStringWhereUsedEventsAndListStore(type_name);
            makeRequestForTypeFind(setHashDataForTypeFind(), state);
        }
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
        String lastUrl = "StoreInfoFindByType.do?";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(ListStorePage.this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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

    private void makeRequestForSearch(final int state){
        //String url = "http://54.180.56.44:8080/StoreSearch.do?keyword="+storeSearch;
        //URL : http://15.165.22.64:8080/StoreSearch.do?keyword=test&startPoint=0
        HashMap<String,Object> data = new HashMap<>();
        data.put("latitude",latLng.latitude);
        data.put("longitude",latLng.longitude);
        data.put("keyword",storeSearch);
        data.put("startPoint",currentPos);
//        String url = new UrlMaker().UrlMake("StoreSearch.do?keyword="+ storeSearch+"&startPoint=" +currentPos);
        String url = new UrlMaker().UrlMake("StoreSearchByKeyword.do");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        jsonParsing(response.toString(), state);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("searchStoreError", "error");
            }
        });
        requestQueue.add(jsonObjectRequest);
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
//        ArrayList<ListStoreHelperClass> DataList = new ArrayList<>();
//        ArrayList<ListStoreHelperClass> DataListForIsOpen = new ArrayList<>();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

//        int [] storeIds = new int[listStoreParsing.store.size()];
//        for(int i = 0; i< listStoreParsing.store.size();i++){
//
//            ListStoreHelperClass listStoreHelperClass = new ListStoreHelperClass(listStoreParsing.store.get(i).getStore_name(), listStoreParsing.store.get(i).getStore_location(), listStoreParsing.store.get(i).getStore_image(), listStoreParsing.store.get(i).getDistance(), listStoreParsing.store.get(i).getStore_id(), listStoreParsing.store.get(i).getIs_open());
//
//            listStoreHelperClass.storeNames.add(listStoreParsing.store.get(i).getStore_name());
//            listStoreHelperClass.storeLocations.add(listStoreParsing.store.get(i).getStore_location());
//            listStoreHelperClass.storeImages.add(listStoreParsing.store.get(i).getStore_image());
//            listStoreHelperClass.storeDistances.add(listStoreParsing.store.get(i).getDistance());
//            listStoreHelperClass.storeIds.add(listStoreParsing.store.get(i).getStore_id());
//            storeIds[i] = listStoreParsing.store.get(i).getStore_id();
//
//            listStoreHelperClass.storesIsOpen.add(listStoreParsing.store.get(i).getIs_open());
////            if(listStoreParsing.store.get(i).getIs_open().equals("Y")) {
////                DataListForIsOpen.add(listStoreHelperClass);
////                continue;
////            }
//            DataList.add(listStoreHelperClass);
//        }
//        DataListForIsOpen.addAll(DataList);
//        DataList.addAll(DataList);
        SharedPreferences getStoreId = getSharedPreferences("storeID", MODE_PRIVATE);
        getStoreId.edit().putString("listStore", listStoreParsing.toString()).apply();
        getStoreId.edit().putInt("currentPos", currentPos);
        getStoreId.edit().apply();
        getStoreId.edit().commit();

//        adapter = new ListStoreAdapter(DataListForIsOpen, this, this,  this);
        adapter = new StoreListAdapter(listStoreParsing, this,  this);
        mRecyclerView.setAdapter(adapter);
        progressApplication.progressOFF();
        refreshLayout.setRefreshing(false);
    }

    private void jsonParsing(String result, int state) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(!jsonObject.getBoolean("result")) {
                Toast.makeText(this, "가게정보가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                progressApplication.progressOFF();
                refreshLayout.setRefreshing(false);
                return;
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
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
    public void onItemSelected(View v, int position) {

        StoreListAdapter.ListStoreViewHolder listStoreViewHolder = (StoreListAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(ListStorePage.this, StoreInfoReNewer.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        if(listStoreViewHolder.storeId == null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(ListStorePage.this, "해당 가게가 존재하지 않습니다 재접속 해주세요", Toast.LENGTH_LONG);
                }
            });
        }


        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        intent.putExtra("discount_rate", Integer.parseInt(listStoreViewHolder.discountRate.getText().toString().substring(5, listStoreViewHolder.discountRate.getText().toString().lastIndexOf("%"))));
        startActivity(intent);
        CustomIntent.customType(this,"left-to-right");
    }
    @Override
    public void onBack() {
        onBackPressed();
        CustomIntent.customType(this,"right-to-left");
//        overridePendingTransition(R.anim.right_to_left, R.anim.left_to_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CustomIntent.customType(this,"right-to-left");
    }
}