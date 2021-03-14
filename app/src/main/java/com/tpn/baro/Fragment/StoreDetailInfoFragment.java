package com.tpn.baro.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.GoogleMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.tpn.baro.HelperDatabase.StoreDetail;
import com.tpn.baro.R;
import com.tpn.baro.Url.UrlMaker;

public class StoreDetailInfoFragment extends Fragment implements OnMapReadyCallback {
    MapFragment mapFragment;
    GoogleMap map;
    NaverMap naverMap;
    Marker marker;
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

    TextView representativeName;
    TextView representativePhone;
    TextView representativeNumber;

    RelativeLayout eventsBenefitsLayout;
    //
    View rootView;

    int storeId;

    LatLng latLng;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_detail, container, false);
        storeInfo = rootView.findViewById(R.id.store_infomation);
        openCloseTime = rootView.findViewById(R.id.open_close);
        daysOff = rootView.findViewById(R.id.days_off);
        storePhone = rootView.findViewById(R.id.store_phone);
        storeLocation = rootView.findViewById(R.id.store_location);

        storeIntro = rootView.findViewById(R.id.store_title);
        eventBenefit = rootView.findViewById(R.id.events_benefits_title);
        storeInfoTitle = rootView.findViewById(R.id.store_info_title);
        eventsBenefitsLayout = rootView.findViewById(R.id.events_benefits_layout);

        representativeName = rootView.findViewById(R.id.representative_name_content);
        representativePhone = rootView.findViewById(R.id.representative_phone_content);
        representativeNumber = rootView.findViewById(R.id.representative_number_content);

        FragmentManager fm = getChildFragmentManager();
        mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        storeId = Integer.parseInt(intent.getStringExtra("store_id"));
        if(storeId == 0){
            Toast.makeText(getActivity(), "로딩 오류 입니다.", Toast.LENGTH_LONG).show();
            return;
        }
        //jsonParsing(string storeDetail);

    }

    private void jsonParsing(String storeDetail, NaverMap naverMap) {
        Gson gson = new GsonBuilder().create();
        storeDetailData = gson.fromJson(storeDetail, StoreDetail.class);

        LatLng storeLocation = new LatLng(storeDetailData.getStore_latitude(),storeDetailData.getStore_longitude());
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(storeLocation);
        this.naverMap.moveCamera(cameraUpdate);
        marker = new Marker();
        marker.setPosition(storeLocation);

        int height = getResources().getDrawable(R.drawable.store_marker).getIntrinsicHeight();
        int width = getResources().getDrawable(R.drawable.store_marker).getIntrinsicWidth();
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.store_marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        marker.setIcon(OverlayImage.fromBitmap(smallMarker));

        marker.setMap(naverMap);
        marker.setCaptionText(storeDetailData.getStore_name());

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

//        storeIntro.setPaintFlags(storeIntro.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        eventBenefit.setPaintFlags(eventBenefit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        eventsBenefitsLayout.setVisibility(View.GONE);


        representativeName.setText(storeDetailData.getRepresentative_name());
        representativePhone.setText(storeDetailData.getStore_phone());
        representativeNumber.setText(storeDetailData.getBusiness_number());
//        storeInfoTitle.setPaintFlags(eventBenefit.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
//        mapReady();
    }
//    public void mapReady() {
//        latLng = new LatLng(storeDetailData.getStore_latitude(), storeDetailData.getStore_longitude());
//        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(new OnMapReadyCallback() {
//            @Override
//            public void onMapReady(GoogleMap googleMap) {
//                Log.d("Map", "지도 준비됨.");
//                map = googleMap;
//                try {
//                    map.setMyLocationEnabled(true);
//                }
//                catch(SecurityException e){
//                    e.printStackTrace();
//                }
//                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
//
//                MarkerOptions markerOption = new MarkerOptions().position(latLng);
//                int height = 86;
//                int width = 69;
//                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_purple);
//                Bitmap b = bitmapdraw.getBitmap();
//                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//                markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
//
//                View marker = ((LayoutInflater) getContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_textview, null);
//                TextView storeNameOfMarker = (TextView) marker.findViewById(R.id.store_title);
//                TextView storeLocationOfMarker = (TextView) marker.findViewById(R.id.store_location_content);
//
//                storeNameOfMarker.setText(storeDetailData.getStore_name());
//                String locationName = storeDetailData.getStore_location();
//                StringTokenizer stringTokenizer = new StringTokenizer(locationName, " ");
//                StringBuilder stringBuilder = new StringBuilder();
//                int i = 0;
//                while(stringTokenizer.hasMoreElements()) {
//                    stringBuilder.append(stringTokenizer.nextElement()+" ");
//                    i++;
//                    if(i > 4){
//                        stringBuilder.append("\n");
//                    }
//                }
//                storeNameOfMarker.setText(storeDetailData.getStore_name());
//                storeLocationOfMarker.setText(stringBuilder.toString());
//
//                MarkerOptions markerOptions2 = new MarkerOptions().position(latLng).anchor(0f, 0.1f);
//                markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker)));
//
//                map.addMarker(markerOptions2).showInfoWindow();
//                map.addMarker(markerOption).showInfoWindow();
//
//            }
//        });
//    }
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }
    public void makeRequestGetStore(final int number,final NaverMap naverMap) {
        String url = new UrlMaker().UrlMake("StoreFindById.do?store_id="+ number);
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsing(response, naverMap);
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


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        makeRequestGetStore(storeId, naverMap);
    }
}