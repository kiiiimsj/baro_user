package com.tpn.baro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Adapter.ListStoreAdapter;
import com.tpn.baro.AdapterHelper.FavoriteHelperClass;
import com.tpn.baro.AdapterHelper.ListStoreHelperClass;
import com.tpn.baro.Database.SessionManager;
import com.tpn.baro.JsonParsingHelper.FavoriteListParsing;
import com.tpn.baro.JsonParsingHelper.ListStoreParsing;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.myGPSListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListStoreFavoritePage extends AppCompatActivity implements ListStoreAdapter.OnListItemLongSelectedInterface, ListStoreAdapter.OnListItemSelectedInterface {
    final private String TAG = this.getClass().getSimpleName();
    ImageView backButton;

    RecyclerView mRecyclerView;
    ListStoreAdapter adapter;

    LatLng latLng;

    //즐겨찾기
    SessionManager sessionManager;
    SharedPreferences sp;
    Gson gson;
    String phone;
    ArrayList<FavoriteListParsing> favoriteListParsings;
    ProgressApplication progressApplication;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_store_favorite_page);
        progressApplication = new ProgressApplication();
        progressApplication.progressON(this);
        mRecyclerView = findViewById(R.id.recyclerView);
        backButton = findViewById(R.id.back_pressed);

        gson = new GsonBuilder().create();
        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> hashMap = sessionManager.getUsersDetailFromSession();
        myGPSListener myGPSListener = new myGPSListener(this);
        latLng = myGPSListener.startLocationService(null);

        phone = hashMap.get(sessionManager.KEY_PHONENUMBER);
        makeRequestForFavorite(phone);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sessionManager = new SessionManager(this, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> hashMap = sessionManager.getUsersDetailFromSession();

        phone = hashMap.get(sessionManager.KEY_PHONENUMBER);
        makeRequestForFavorite(phone);
    }
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }
    private void mRecyclerView2(){
        mRecyclerView.setHasFixedSize(true);
        ArrayList<ListStoreHelperClass> DataList = new ArrayList<>();
        ArrayList<ListStoreHelperClass> DataListForIsOpen = new ArrayList<>();
        HashMap<String, FavoriteHelperClass> hashMap = new HashMap<>();

        hashMap.put("FavoriteHelperClass", new FavoriteHelperClass("","","","","","","",0.0));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        for(int i = 0; i<favoriteListParsings.size();i++){

            ListStoreHelperClass listStoreHelperClass = new ListStoreHelperClass(favoriteListParsings.get(i).getStore_name(),favoriteListParsings.get(i).getStore_location(),favoriteListParsings.get(i).getStore_image(),favoriteListParsings.get(i).getDistance(), favoriteListParsings.get(i).getStore_id(), favoriteListParsings.get(i).getStore_is_open());

            listStoreHelperClass.storeNames.add(favoriteListParsings.get(i).getStore_name());
            listStoreHelperClass.storeLocations.add(favoriteListParsings.get(i).getStore_location());
            listStoreHelperClass.storeImages.add(favoriteListParsings.get(i).getStore_image());
            listStoreHelperClass.storeDistances.add(favoriteListParsings.get(i).getDistance());
            listStoreHelperClass.storeIds.add(favoriteListParsings.get(i).getStore_id());
            listStoreHelperClass.storesIsOpen.add(favoriteListParsings.get(i).getStore_is_open());
            if(favoriteListParsings.get(i).getStore_is_open().equals("Y")) {
                DataListForIsOpen.add(listStoreHelperClass);
                continue;
            }
            DataList.add(listStoreHelperClass);
        }
        DataListForIsOpen.addAll(DataList);
        adapter = new ListStoreAdapter(DataListForIsOpen, this, this, this);
        mRecyclerView.setAdapter(adapter);
        progressApplication.progressOFF();
    }
    private void jsonParsing(String result) {
        ListStoreParsing listStoreParsing = new ListStoreParsing();
        JSONArray jsonArray = null;
        favoriteListParsings = new ArrayList<>();
        boolean result2 = false;
        try {
            result2 = (Boolean) new JSONObject(result).getBoolean("result");
            listStoreParsing.setMessage(new JSONObject(result).getString("message"));
            jsonArray = new JSONObject(result).getJSONArray("favorite");
            if (result2 == false || jsonArray == null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ListStoreFavoritePage.this, "가게정보를 받아올 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jObject = jsonArray.getJSONObject(i);
                String store_name = jObject.optString("store_name");
                String store_latitude = jObject.optString("store_latitude");
                String store_longitude = jObject.optString("store_longitude");
                String store_image = jObject.optString("store_image");
                String store_info = jObject.optString("store_info");
                String store_is_open = jObject.optString("is_open");
                float distance = (float)jObject.optDouble("distance");

                Log.e("storeImageee", store_image);

                String store_location = jObject.optString("store_location");
                int store_id = jObject.optInt("store_id");
                FavoriteListParsing favoriteListParsing = new FavoriteListParsing(store_id, store_info, store_latitude, store_longitude, store_name, store_location, store_image, store_is_open, distance);
                Log.i("LISTSTORELISTPARSING", favoriteListParsing.toString());
                favoriteListParsings.add(favoriteListParsing);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        //listStoreParsing.setStoreLists(listStoreListParsings);
        mRecyclerView2();
    }

    private void makeRequestForFavorite(String phone) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("phone",phone);
            jsonObject.put("latitude",latLng.latitude);
            jsonObject.put("longitude",latLng.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String lastUrl = "FavoriteList.do?";
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("url", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("favoritefirst", response.toString());
                        sp = getSharedPreferences("favorite", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("favorite", response.toString());
                        editor.apply();
                        editor.commit();
                        jsonParsing(response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("favoritefirst", "error");
                    }
                });
        requestQueue.add(request);
    }

    @Override
    public void onItemLongSelected(View v, int adapterPosition) {
        ListStoreAdapter.ListStoreViewHolder listStoreViewHolder = (ListStoreAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(adapterPosition);
        Intent intent = new Intent(ListStoreFavoritePage.this, StoreInfoReNewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        intent.putExtra("store_name", listStoreViewHolder.storeName.getText().toString());
        startActivity(intent);
    }

    @Override
    public void onItemSelected(View v, int position) {
        ListStoreAdapter.ListStoreViewHolder listStoreViewHolder = (ListStoreAdapter.ListStoreViewHolder)mRecyclerView.findViewHolderForAdapterPosition(position);
        Intent intent = new Intent(ListStoreFavoritePage.this, StoreInfoReNewer.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("store_id", listStoreViewHolder.storeId.getText().toString());
        Log.i("storeId", listStoreViewHolder.storeId.getText().toString());
        startActivity(intent);
    }
    private double getDistance(Location myLocation, Location storeLocation){
        double distance;
        distance = myLocation.distanceTo(storeLocation);
        return distance;
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainPage.class));
        finish();
    }
}