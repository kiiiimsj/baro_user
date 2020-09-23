package com.example.wantchu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.Url.UrlMaker;
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

public class StoreDetailInfoFragment extends Fragment {
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

    //
    View rootView;

    LatLng latLng;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_store_detail, container, false);
        storeInfo = rootView.findViewById(R.id.store_info);
        openCloseTime = rootView.findViewById(R.id.open_close);
        daysOff = rootView.findViewById(R.id.days_off);
        storePhone = rootView.findViewById(R.id.store_phone);
        storeLocation = rootView.findViewById(R.id.store_location);

        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        String storeId =intent.getStringExtra("store_id");
        if(storeId.equals("")){
            Toast.makeText(getActivity(), "로딩 오류 입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        //jsonParsing(string storeDetail);
        makeRequestGetStore(Integer.parseInt(storeId));
    }

    private void jsonParsing(String storeDetail) {
        Log.i("JSONPARSING", storeDetail);
        Gson gson = new GsonBuilder().create();
        storeDetailData = gson.fromJson(storeDetail, StoreDetail.class);
        setActivity();
    }
    public void setActivity() {
        StringBuilder closeOpenTime = new StringBuilder(storeDetailData.getStore_opentime()+" ~ "
                +storeDetailData.getStore_closetime());
        storeInfo.setText(storeDetailData.getStore_info());
        openCloseTime.setText(closeOpenTime.toString());
        daysOff.setText(storeDetailData.getStore_daysoff());
        storePhone.setText(storeDetailData.getStore_phone());
        storeLocation.setText(storeDetailData.getStore_location());
        mapReady();
    }
    public void mapReady() {
        latLng = new LatLng(storeDetailData.getStore_latitude(), storeDetailData.getStore_longitude());
        myLocationMarker = new MarkerOptions();
        myLocationMarker.position(latLng).title(storeDetailData.getStore_name());
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
                map.addMarker(myLocationMarker).showInfoWindow();
            }
        });
    }
    public void makeRequestGetStore(final int number) {
        String url = new UrlMaker().UrlMake("StoreFindById.do?store_id="+ number);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsing(response);
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
}