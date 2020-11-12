package com.example.wantchu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.JsonParsingHelper.MapListParsing;
import com.example.wantchu.JsonParsingHelper.MapParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapSdk;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.geometry.*;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class NewMyMap extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    myGPSListener GPSListener;
    LatLng myLatlng;
    RelativeLayout storeDetail;
    Boolean isMarkerClicked = false;
    Marker currentOpenMarker = null;
    StoreDetail storeDetailData;
    TextView storeTitle;
    ImageView storePreview;
    TextView distance;
    TextView storeAddress;
    Boolean willChange = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_my_map);
        mapView = findViewById(R.id.map_view);
        storeDetail = findViewById(R.id.store_detail);
        storeTitle = findViewById(R.id.store_title);
        storePreview = findViewById(R.id.store_preview);
        distance = findViewById(R.id.distance);
        storeAddress = findViewById(R.id.address);
        mapView.getMapAsync(this);
        mapView.onCreate(savedInstanceState);
        GPSListener = new myGPSListener(NewMyMap.this);
        myLatlng = GPSListener.startLocationService(null);
        locationSource =
                new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }
    public HashMap setHashMapData() {
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("latitude", myLatlng.latitude);
        hash.put("longitude", myLatlng.longitude);

        return hash;
    }
    private void makeRequest(HashMap data, final NaverMap naverMap){
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreAllLocation.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("map", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RESPONSE", response.toString());
                jsonParsing(response.toString(), naverMap);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }
    private void jsonParsing(String result,NaverMap naverMap){
        MapParsing mapParsing = new MapParsing();
        try{
            Boolean result2 = new JSONObject(result).getBoolean("result");
            mapParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("store");
            ArrayList<MapListParsing> mapListParsings = new ArrayList<>();

            for(int i = 0; i < jsonArray.length();i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                int store_id = jObject.optInt("store_id");
                String store_name = jObject.optString("store_name");
                String store_latitude = jObject.optString("store_latitude");
                String store_longitude = jObject.optString("store_longitude");
                float store_distance = (float)jObject.optDouble("distance");
                MapListParsing mapListParsing = new MapListParsing(store_id, store_name, store_latitude, store_longitude, store_distance);
                mapListParsings.add(mapListParsing);
                Log.e("realMap", result2.toString());
            }
            mapParsing.setMapList(mapListParsings);
        }
        catch(JSONException e){
            e.printStackTrace();

        }
        mapInfo(mapParsing,naverMap);
    }
    private void mapInfo(MapParsing mapParsing,NaverMap naverMap){
        naverMap.setOnMapClickListener(new NaverMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull PointF pointF, @NonNull com.naver.maps.geometry.LatLng latLng) {
                Log.d("tttt", willChange+"");
                if (willChange){
                    if (isMarkerClicked){
                        isMarkerClicked = false;
                        currentOpenMarker = null;
                        storeDetail.setVisibility(View.GONE);
                    }
                }
                willChange = true;
            }
        });
        for (final MapListParsing item : mapParsing.getMapList()) {
            final Marker marker = new Marker();
            com.naver.maps.geometry.LatLng storeLatlng = new com.naver.maps.geometry.LatLng(new Double(item.getStore_latitude()),new Double(item.getStore_longitude()));
            marker.setPosition(storeLatlng);
            int height = 27;
            int width = 20 ;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_purple);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            marker.setIcon(OverlayImage.fromBitmap(smallMarker));
            marker.setMap(naverMap);
            marker.setCaptionText(item.getStore_name());
            marker.setOnClickListener(new Overlay.OnClickListener() {
                @Override
                public boolean onClick(@NonNull Overlay overlay) {
                    Log.i("tttt","qwerqwe");
                    if (currentOpenMarker == overlay) {
                        currentOpenMarker = null;
                        isMarkerClicked = false;
                        storeDetail.setVisibility(View.GONE);
                    }else{
                        if (!isMarkerClicked) {
                            storeDetail.setVisibility(View.VISIBLE);
                            isMarkerClicked = true;
                        }
                        makeRequestForStoreDetail(""+item.getStore_id(),item.getDistance());
                        willChange = false;
                    }

                    return false;
                }
            });
        }
    }
    private void makeRequestForStoreDetail(String storeInfo, final Double distance) {
        String url = new UrlMaker().UrlMake("StoreFindById.do?store_id=" + storeInfo);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                storeParsing(response,""+distance);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }
    public void makeRequestForImage(final String distanceStr) {
        String url = new UrlMaker().UrlMake("ImageStore.do?image_name="+storeDetailData.getStore_image());
        Log.i("store", url);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        setStoreInfo(distanceStr, response);
                    }
                }, 200, 200, ImageView.ScaleType.FIT_XY, null,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("error", "error");
                    }
                });
        requestQueue.add(request);
    }
    private void storeParsing(String response, String distance) {
        Gson gson = new GsonBuilder().create();
        storeDetailData = gson.fromJson(response, StoreDetail.class);
        makeRequestForImage(distance);

    }
    private void setStoreInfo(String distanceStr, Bitmap response) {
        storePreview.setImageBitmap(response);
        storeTitle.setText(storeDetailData.getStore_name());
        storeAddress.setText(storeDetailData.getStore_location());
        distance.setText(((int)Double.parseDouble(distanceStr))+"m");
        storeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NewMyMap.this, StoreInfoReNewer.class);
                intent.putExtra("store_id", storeDetailData.getStore_id()+"");
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
//        Marker marker = new Marker();
        com.naver.maps.geometry.LatLng naverLatlng;
        naverLatlng = new com.naver.maps.geometry.LatLng(myLatlng.latitude,myLatlng.longitude);
        locationOverlay.setPosition(naverLatlng);
//        marker.setPosition(naverLatlng);
//        marker.setMap(naverMap);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(naverLatlng);
        naverMap.moveCamera(cameraUpdate);
        makeRequest(setHashMapData(),naverMap);
    }
}
