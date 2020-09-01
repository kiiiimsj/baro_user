package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

//import com.example.wantchu.Database.SendToServer;

public class ListStorePage extends AppCompatActivity implements ListStoreAdapter.OnListItemLongSelectedInterface, ListStoreAdapter.OnListItemSelectedInterface , AutoPermissionsListener {
    RelativeLayout mapBar;
    ImageView backButton;

    RecyclerView mRecyclerView;
    ListStoreAdapter adapter;

    TextView typeName;
    TextView mAddress;

    //메인페이지에서 넘어온 리스트에 필요한 값들
    String type_code;
    String type_name;

    //검색을 통해서 넘어온 리스트에 필요한 값
    String storeSearch;

    LatLng latLng;

    StoreSessionManager storeSessionManager;
    ArrayList<ListStoreListParsing> listStoreListParsings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("CREATE_LISTSTORE", 1+"");
        setContentView(R.layout.activity_list_store_page);
        mRecyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.back_pressed);
        typeName = findViewById(R.id.type_name);
        mAddress = findViewById(R.id.address);
        mapBar = findViewById(R.id.map_bar);

        myGPSListener myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(mAddress);

        storeSessionManager = new StoreSessionManager(getApplicationContext(), StoreSessionManager.STORE_SESSION);

        mapBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MyMap.class));
            }
        });


        chooseShowList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        chooseShowList();
    }

    public void chooseShowList() {
        Intent intent = getIntent();
        Log.i("PASS", "PASS5");
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
            makeRequestForTypeFind();
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
    private void makeRequestForTypeFind() {
        String lastUrl = "StoreInfoFindByType.do?type_code=" + type_code;
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        Log.i("storesList", "request made to " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("storeList", response);
                        jsonParsing(response);
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
                        jsonParsing(response);
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

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Location myLocation = new Location("");

        myLocation.setLatitude(latLng.latitude);
        myLocation.setLongitude(latLng.longitude);
        int [] storeIds = new int[listStoreListParsings.size()];
        for(int i = 0; i< listStoreListParsings.size();i++){

            Location storeLocation = new Location("");
            storeLocation.setLatitude(Double.parseDouble(listStoreListParsings.get(i).getStoreLatitude()));
            storeLocation.setLongitude(Double.parseDouble(listStoreListParsings.get(i).getStoreLongitude()));
            double distance = getDistance(myLocation, storeLocation);

            Log.e("dist", String.valueOf(distance));

            ListStoreHelperClass listStoreHelperClass = new ListStoreHelperClass(listStoreListParsings.get(i).getStoreName(), listStoreListParsings.get(i).getStoreLocation(), listStoreListParsings.get(i).getStoreImage(),distance, listStoreListParsings.get(i).getStoreId(), listStoreListParsings.get(i).getStoreIsOpen());

            Log.e("hey", listStoreListParsings.get(i).getStoreImage());

            listStoreHelperClass.storeNames.add(listStoreListParsings.get(i).getStoreName());
            listStoreHelperClass.storeLocations.add(listStoreListParsings.get(i).getStoreLocation());
            listStoreHelperClass.storeImages.add(listStoreListParsings.get(i).getStoreImage());
            listStoreHelperClass.storeDistances.add(distance);
            listStoreHelperClass.storeIds.add(listStoreListParsings.get(i).getStoreId());
            storeIds[i] = listStoreListParsings.get(i).getStoreId();

            listStoreHelperClass.storesIsOpen.add(listStoreListParsings.get(i).getStoreIsOpen());

            DataList.add(listStoreHelperClass);
        }
        SharedPreferences getStoreId = getSharedPreferences("storeID", MODE_PRIVATE);
        getStoreId.edit().putString("storeid", storeIds.toString()).apply();
        getStoreId.edit().apply();
        getStoreId.edit().commit();
        Collections.sort(DataList);

        adapter = new ListStoreAdapter(DataList, this, this,  this);
        mRecyclerView.setAdapter(adapter);
    }

    private void jsonParsing(String result) {
        //ListStoreParsing listStoreParsing = new ListStoreParsing();
        JSONArray jsonArray = null;
        listStoreListParsings = new ArrayList<>();
        jsonArray = jsonParsingType(result);
        try {
            if(jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jObject = jsonArray.getJSONObject(i);
                    String store_name = jObject.optString("store_name");
                    String store_latitude = jObject.optString("store_latitude");
                    String store_longitude = jObject.optString("store_longitude");
                    String store_image = jObject.optString("store_image");
                    String store_open = jObject.optString("is_open");

                    Log.e("storeImageee", store_image);

                    String store_location = jObject.optString("store_location");
                    int store_id = jObject.optInt("store_id");
                    ListStoreListParsing listStoreListParsing = new ListStoreListParsing(store_name, store_latitude, store_longitude, store_location, store_image, store_id, store_open);
                    Log.i("LISTSTORELISTPARSING", listStoreListParsing.toString());
                    listStoreListParsings.add(listStoreListParsing);
                }
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //listStoreParsing.setStoreLists(listStoreListParsings);
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
        Intent intent = new Intent(getApplicationContext(), StoreInfo.class);
        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        intent.putExtra("store_name", listStoreViewHolder.storeName.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(View v, int position) {
        ListStoreAdapter.ListStoreViewHolder listStoreViewHolder = (ListStoreAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(getApplicationContext(), StoreInfo.class);
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