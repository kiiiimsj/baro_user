package com.example.wantchu.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class StoreDetailInfoFragment extends Fragment {
    SupportMapFragment mapFragment;
    GoogleMap map;

    //
    StoreDetail storeDetailData;

    TextView storeInfo;
    TextView openCloseTime;
    TextView daysOff;
    TextView storePhone;
    TextView storeLocation;

    TextView storeIntro;
    TextView eventBenefit;
    TextView storeInfoTitle;

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

        storeIntro = rootView.findViewById(R.id.store_intro_title);
        eventBenefit = rootView.findViewById(R.id.events_benefits_title);
        storeInfoTitle = rootView.findViewById(R.id.store_info_title);


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
        openCloseTime.setText("운영시간\t\t"+closeOpenTime.toString());
        daysOff.setText(storeDetailData.getStore_daysoff());
        storePhone.setText(storeDetailData.getStore_phone());
        storeLocation.setText(storeDetailData.getStore_location());

        storeIntro.setPaintFlags(storeIntro.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        eventBenefit.setPaintFlags(eventBenefit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        storeInfoTitle.setPaintFlags(eventBenefit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        mapReady();
    }
    public void mapReady() {
        latLng = new LatLng(storeDetailData.getStore_latitude(), storeDetailData.getStore_longitude());
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
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                MarkerOptions markerOption = new MarkerOptions().position(latLng);
                int height = 110;
                int width = 80;
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_purple);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(storeDetailData.getStore_location());
                map.addMarker(markerOption).showInfoWindow();
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