package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.JsonParsingHelper.MapListParsing;
import com.example.wantchu.JsonParsingHelper.MapParsing;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MyMap extends AppCompatActivity implements AutoPermissionsListener {

    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions myLocationMarker;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);

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
                makeRequest();
            }
        });

        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        latLng = startLocationService();

        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    private void makeRequest(){
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreAllLocation.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("map", url);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mapInfo(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("map", "error");
                    }
                });
        requestQueue.add(request);
    }

    private void mapInfo(String result){
        ArrayList<MapListParsing> DataList = new ArrayList<>();


        MapParsing mapParsing = new MapParsing();
        jsonParsing(result, mapParsing);
        MapListParsing mapListParsing;
        Location myLocation = new Location("");
        myLocation.setLatitude(latLng.latitude);
        myLocation.setLongitude(latLng.longitude);
        for(int i = 0; i < mapParsing.getMapList().size();i++){
            mapListParsing = mapParsing.getMapList().get(i);
            Location storeLocation = new Location("");
            storeLocation.setLatitude(Double.parseDouble(mapListParsing.getStore_latitude()));
            storeLocation.setLongitude(Double.parseDouble(mapListParsing.getStore_longitude()));
            double distance = getDistance(myLocation, storeLocation);
            mapListParsing = new MapListParsing(mapListParsing.getStore_name(), mapListParsing.getStore_latitude(), mapListParsing.getStore_longitude(), distance);
            DataList.add(mapListParsing);
        }
        Collections.sort(DataList);

        MarkerOptions markerOptions = null;
        //마커 여러개 만들기

        for(int i = 0; i< DataList.size(); i++){
            MapListParsing store = DataList.get(i);
            Double lati = Double.parseDouble(store.getStore_latitude());
            Double logi = Double.parseDouble(store.getStore_longitude());
            String name = store.getStore_name();
            markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(lati, logi)).title(name);
            map.addMarker(markerOptions).showInfoWindow();
        }

    }

    private synchronized void jsonParsing(String result, MapParsing mapParsing){
        try{
            Boolean result2 = new JSONObject(result).getBoolean("result");
            mapParsing.setMessage(new JSONObject(result).getString("message"));
            JSONArray jsonArray = new JSONObject(result).getJSONArray("store");
            ArrayList<MapListParsing> mapListParsings = new ArrayList<>();

            for(int i = 0; i < jsonArray.length();i++){
                JSONObject jObject = jsonArray.getJSONObject(i);
                String store_name = jObject.optString("store_name");
                String store_latitude = jObject.optString("store_latitude");
                String store_longitude = jObject.optString("store_longitude");
                MapListParsing mapListParsing = new MapListParsing(store_name, store_latitude, store_longitude, 0.0);
                mapListParsings.add(mapListParsing);
                Log.e("realMap", result2.toString());
            }
            mapParsing.setMapList(mapListParsings);
        }
        catch(JSONException e){
            e.printStackTrace();

        }
    }

    public LatLng startLocationService() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LatLng latLng = null;
        try {
            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
                Log.d("Map", message);

                Geocoder geocoder = new Geocoder(this, Locale.KOREA);
                try {
                    List<Address> list = geocoder.getFromLocation(latitude, longitude, 10);
                    Log.d("MyMapbb", list.get(0).getAddressLine(0));
                }
                catch(Exception e){
                    e.printStackTrace();
                }
            }

            GPSListener gpsListener = new GPSListener();
            long minTime = 10000;
            float minDistance = 0;

            manager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime, minDistance, gpsListener);

            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        } catch(SecurityException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    public void onClickBack(View view) {
        super.onBackPressed();
    }

    class GPSListener implements LocationListener {
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;

            showCurrentLocation(latitude, longitude);

        }

        public void onProviderDisabled(String provider) { }

        public void onProviderEnabled(String provider) { }

        public void onStatusChanged(String provider, int status, Bundle extras) { }
    }

    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
        showMyLocationMarker(curPoint);
    }

    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions();
            myLocationMarker.position(curPoint);
            map.addMarker(myLocationMarker);
        } else {
            myLocationMarker.position(curPoint);
        }
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
    private double getDistance(Location myLocation, Location storeLocation){
        double distance;
        distance = myLocation.distanceTo(storeLocation);
        return distance;
    }

}