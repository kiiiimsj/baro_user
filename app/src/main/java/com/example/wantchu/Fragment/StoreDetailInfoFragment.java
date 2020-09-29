package com.example.wantchu.Fragment;

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
import android.widget.LinearLayout;
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

import java.util.StringTokenizer;

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
                int height = 86;
                int width = 69;
                BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_purple);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
                markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                View marker = ((LayoutInflater) getContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE)).inflate(R.layout.map_marker_textview, null);
                TextView storeNameOfMarker = (TextView) marker.findViewById(R.id.store_title);
                TextView storeLocationOfMarker = (TextView) marker.findViewById(R.id.store_location_content);

                storeNameOfMarker.setText(storeDetailData.getStore_name());
                String locationName = storeDetailData.getStore_location();
                StringTokenizer stringTokenizer = new StringTokenizer(locationName, " ");
                StringBuilder stringBuilder = new StringBuilder();
                int i = 0;
                while(stringTokenizer.hasMoreElements()) {
                    stringBuilder.append(stringTokenizer.nextElement()+" ");
                    i++;
                    if(i > 4){
                        stringBuilder.append("\n");
                    }
                }
                storeNameOfMarker.setText(storeDetailData.getStore_name());
                storeLocationOfMarker.setText(stringBuilder.toString());

                MarkerOptions markerOptions2 = new MarkerOptions().position(latLng).anchor(0f, 0.1f);
                markerOptions2.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(getActivity(), marker)));

                map.addMarker(markerOptions2).showInfoWindow();
                map.addMarker(markerOption).showInfoWindow();

            }
        });
    }
    private Bitmap createDrawableFromView(Context context, View view) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
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