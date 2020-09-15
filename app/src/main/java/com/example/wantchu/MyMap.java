package com.example.wantchu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Dialogs.MapSetPositionDialog;
import com.example.wantchu.JsonParsingHelper.MapListParsing;
import com.example.wantchu.JsonParsingHelper.MapParsing;
import com.example.wantchu.Url.UrlMaker;
import com.example.wantchu.helperClass.myGPSListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class MyMap extends AppCompatActivity implements AutoPermissionsListener, MapSetPositionDialog.isClickOkay {

    TextView title;
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions oldMarkerOption;
    MarkerOptions currentMarkerOption;

    Marker newMarker;

    MarkerOptions myLocationMarker;


    LatLng oldLatLng;

    String where;
    TextView address;

    MapSetPositionDialog mapSetPositionDialog;
    SharedPreferences setMyNewLocation;
    myGPSListener GPSListener;
    SharedPreferences.Editor editor;
    Bundle myMapBundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        setMyNewLocation = getSharedPreferences("newLocation", MODE_PRIVATE);
        GPSListener = new myGPSListener(this);
        myMapBundle = new Bundle();
        title = findViewById(R.id.type_name);
        address = findViewById(R.id.show_latlng_address);
        oldMarkerOption = new MarkerOptions();
        mapSetPositionDialog = new MapSetPositionDialog(this, this);

        editor = setMyNewLocation.edit();
        Intent intent = getIntent();
        where = intent.getStringExtra("from");

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;
                oldLatLng = GPSListener.startLocationService(null);

                GPSListener.setMapLocationTextView(address, oldLatLng);
                showCurrentLocation(oldLatLng.latitude, oldLatLng.longitude);

                try {
                    //map.setMyLocationEnabled(true);
                    map.moveCamera(CameraUpdateFactory.newLatLng(oldLatLng));
                    map.setContentDescription("현재 설정 위치");
                    map.setMyLocationEnabled(true);
                    map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                        @Override
                        public void onMapClick(final LatLng latLng) {
                            map.clear();

                            oldMarkerOption.position(oldLatLng).title("현재 위치");
                            Marker oldMarker = map.addMarker(oldMarkerOption);
                            oldMarker.showInfoWindow();
                            map.addMarker(oldMarkerOption);

                            currentMarkerOption = new MarkerOptions().position(latLng).title("현재 위치로 설정");
                            Marker showMaker = map.addMarker(currentMarkerOption);

                            GPSListener.setMapLocationTextView(address, latLng);
                            showMaker.showInfoWindow();

                            map.addMarker(currentMarkerOption);
                            map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    mapSetPositionDialog.callFunction();
                                    newMarker = marker;
                                }
                            });
                        }
                    });

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

        //latLng = startLocationService();

        Log.i("latLng", oldLatLng +"");
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }
    @Override
    public void clickOkay() {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(newMarker.getPosition().latitude);
        temp.setLongitude(newMarker.getPosition().longitude);
        editor.putString("location", ":"+newMarker.getPosition().latitude+":"+newMarker.getPosition().longitude);
        editor.apply();
        editor.commit();
        Log.i("location_temp", setMyNewLocation.getString("location", null));

        Intent intent = new Intent(getApplicationContext(), MainPage.class);
        startActivity(intent);
        finish();
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
                        jsonParsing(response);
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

    private void mapInfo(MapParsing mapParsing){
        ArrayList<MapListParsing> DataList = new ArrayList<>();
        MapListParsing mapListParsing = new MapListParsing();
        Location myLocation = new Location("");
        myLocation.setLatitude(oldLatLng.latitude);
        myLocation.setLongitude(oldLatLng.longitude);

        for(int i = 0; i < mapParsing.getMapList().size();i++){
            mapListParsing = mapParsing.getMapList().get(i);
            Location storeLocation = new Location("");
            storeLocation.setLatitude(Double.parseDouble(mapListParsing.getStore_latitude()));
            storeLocation.setLongitude(Double.parseDouble(mapListParsing.getStore_longitude()));
            double distance = getDistance(myLocation, storeLocation);
            if(distance >= 1000) {
                continue;
            }
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

    private void jsonParsing(String result){
        MapParsing mapParsing = new MapParsing();
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
        mapInfo(mapParsing);
    }

//    public LatLng startLocationService() {
//        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LatLng latLng = null;
//        try {
//            Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            if (location != null) {
//                double latitude = location.getLatitude();
//                double longitude = location.getLongitude();
//                latLng = new LatLng(latitude, longitude);
//                String message = "최근 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
//                Log.d("Map", message);
//
//                Geocoder geocoder = new Geocoder(this, Locale.KOREA);
//                try {
//                    List<Address> list = geocoder.getFromLocation(latitude, longitude, 10);
//                    Log.d("MyMapbb", list.get(0).getAddressLine(0));
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                }
//            }
//
//            GPSListener gpsListener = new GPSListener();
//            long minTime = 10000;
//            float minDistance = 0;
//
//            manager.requestLocationUpdates(
//                    LocationManager.GPS_PROVIDER,
//                    minTime, minDistance, gpsListener);
//
//            Toast.makeText(getApplicationContext(), "내 위치확인 요청함",
//                    Toast.LENGTH_SHORT).show();
//
//        } catch(SecurityException e) {
//            e.printStackTrace();
//        }
//        return latLng;
//    }
//
    public void onClickBack(View view) {
        super.onBackPressed();
    }
//
//    class GPSListener implements LocationListener {
//        public void onLocationChanged(Location location) {
//            Double latitude = location.getLatitude();
//            Double longitude = location.getLongitude();
//
//            String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
//
//            showCurrentLocation(latitude, longitude);
//
//        }
//
//        public void onProviderDisabled(String provider) { }
//
//        public void onProviderEnabled(String provider) { }
//
//        public void onStatusChanged(String provider, int status, Bundle extras) { }
//    }
//
    private void showCurrentLocation(Double latitude, Double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 17));
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
