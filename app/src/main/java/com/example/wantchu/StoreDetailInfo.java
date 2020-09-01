package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.wantchu.HelperDatabase.StoreDetail;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

public class StoreDetailInfo extends AppCompatActivity {
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myLocationMarker;

    //
    StoreDetail storeDetailData;

    //
    TextView title;
    TextView storeInfo;
    TextView openCloseTime;
    TextView daysOff;
    TextView storePhone;
    TextView storeLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        title = findViewById(R.id.title_content);
        storeInfo = findViewById(R.id.store_info);
        openCloseTime = findViewById(R.id.open_close);
        daysOff = findViewById(R.id.days_off);
        storePhone = findViewById(R.id.store_phone);
        storeLocation = findViewById(R.id.store_location);

        Intent intent = getIntent();
        String storeDetail =intent.getStringExtra("StoreDetail");
        jsonParsing(storeDetail);
        setActivity();

        final LatLng latLng = new LatLng(storeDetailData.getStoreLatitude(), storeDetailData.getStoreLongitude());

        myLocationMarker = new MarkerOptions();
        myLocationMarker.position(latLng).title(storeDetailData.getName());

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;
                try {
                    map.setMyLocationEnabled(true);
                }
                catch(SecurityException e){
                    e.printStackTrace();
                }
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                map.addMarker(myLocationMarker).showInfoWindow();

            }
        });
    }

    private void jsonParsing(String storeDetail) {
        Log.i("JSONPARSING", storeDetail);
        try {
            JSONObject jsonObject = new JSONObject(storeDetail);
            Gson gson = new GsonBuilder().create();
            storeDetailData = gson.fromJson(storeDetail, StoreDetail.class);

            Log.i("JSONOBJECT", storeDetailData.toString());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setActivity() {
        title.setText(storeDetailData.getName());
        StringBuilder closeOpenTime = new StringBuilder(storeDetailData.getStoreOpenTime()+" ~ "
                +storeDetailData.getStoreCloseTime());
        storeInfo.setText(storeDetailData.getStoreInfo());
        openCloseTime.setText(closeOpenTime.toString());
        daysOff.setText(storeDetailData.getStoreDaysoff());
        storePhone.setText(storeDetailData.getStorePhone());
        storeLocation.setText(storeDetailData.getStoreLocation());
    }
    public void onClickBack(View view) {
        super.onBackPressed();
    }
}