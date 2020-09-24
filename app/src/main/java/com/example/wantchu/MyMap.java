package com.example.wantchu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyMap extends AppCompatActivity implements AutoPermissionsListener, MapSetPositionDialog.isClickOkay {

    TextView title;
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions oldMarkerOption;
    Button setMyLocation;
    Button setNewLatLng;
    RelativeLayout OverLayMarker;

    Marker newMarker;


    LatLng oldLatLng;

    TextView address;

    MapSetPositionDialog mapSetPositionDialog;
    SharedPreferences setMyNewLocation;
    myGPSListener GPSListener;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        setMyNewLocation = getSharedPreferences("newLocation", MODE_PRIVATE);


        title = findViewById(R.id.type_name);
        address = findViewById(R.id.show_latlng_address);
        OverLayMarker = findViewById(R.id.over_lay_marker);
        setNewLatLng = findViewById(R.id.set_location);

        setMyLocation = findViewById(R.id.location_change);
        GPSListener = new myGPSListener(MyMap.this);
        mapSetPositionDialog = new MapSetPositionDialog(this, this);

        makeRequest(setHashMapData());
        editor = setMyNewLocation.edit();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        setMapFragmentGetMapAsync();


        Log.i("latLng", oldLatLng +"");
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    private void setMapFragmentGetMapAsync() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                Log.d("Map", "지도 준비됨.");
                map = googleMap;
                oldMarkerOption = new MarkerOptions();
                oldLatLng = GPSListener.startLocationService(null);
                OverLayMarker.setVisibility(View.INVISIBLE);
                setNewLatLng.setVisibility(View.INVISIBLE);
                GPSListener.setMapLocationTextView(address, oldLatLng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLatLng, 15));
                map.getUiSettings().setMapToolbarEnabled(false);
                oldMarkerOption.position(oldLatLng);
                map.addMarker(oldMarkerOption);
                try {
                    map.moveCamera(CameraUpdateFactory.newLatLng(oldLatLng));
                    map.setContentDescription("현재 설정 위치");
                    map.setMyLocationEnabled(true);
                    map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                        @Override
                        public void onInfoWindowClick(Marker marker) {
                            Log.i("STORE_ID", marker.getTag()+"");
                            Intent intent = new Intent(getApplicationContext(), StoreInfoReNewer.class);
                            intent.putExtra("store_id", marker.getTag()+"");
                            startActivity(intent);
                        }
                    });

                    setMyLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setNewLatLng.setVisibility(View.VISIBLE);
                            OverLayMarker.setVisibility(View.VISIBLE);
                            final LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                            final MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                            markerOptions.visible(false);

                            GPSListener.setMapLocationTextView(address, latLng);
                            map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                @Override
                                public void onCameraIdle() {
                                    OverLayMarker.setVisibility(View.VISIBLE);
                                    LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                                    markerOptions.position(latLng);
                                    markerOptions.visible(false);
                                    GPSListener.setMapLocationTextView(address, latLng);
                                }
                            });
                            setNewLatLng.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mapSetPositionDialog.callFunction();
                                    //http://15.165.22.64:8080/StoreFindById.do?store_id=가게id값
                                    newMarker = map.addMarker(markerOptions);
                                }
                            });
                        }
                    });
                }
                catch(SecurityException e){
                    e.printStackTrace();
                }
            }
        });
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void clickOkay() {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(newMarker.getPosition().latitude);
        temp.setLongitude(newMarker.getPosition().longitude);
        OverLayMarker.setVisibility(View.INVISIBLE);
        setNewLatLng.setVisibility(View.INVISIBLE);
        editor.putString("location", ":"+newMarker.getPosition().latitude+":"+newMarker.getPosition().longitude);
        editor.apply();
        editor.commit();
        map.clear();
        makeRequest(setHashMapData());
        setMapFragmentGetMapAsync();
        Log.i("location_temp", setMyNewLocation.getString("location", null));
    }
    public HashMap setHashMapData() {
        HashMap<String, Object> hash = new HashMap<>();
        LatLng setMyNewLatLng = GPSListener.startLocationService(null);
        hash.put("latitude", setMyNewLatLng.latitude);
        hash.put("longitude", setMyNewLatLng.longitude);

        return hash;
    }
    private void makeRequest(HashMap data){
        UrlMaker urlMaker = new UrlMaker();
        String lastUrl = "StoreAllLocation.do";
        String url = urlMaker.UrlMake(lastUrl);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Log.e("map", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.i("RESPONSE", response.toString());
                jsonParsing(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void mapInfo(MapParsing mapParsing){
        ArrayList<MapListParsing> DataList = new ArrayList<>();
        MapListParsing mapListParsing = new MapListParsing();

        for(int i = 0; i < mapParsing.getMapList().size();i++){
            mapListParsing = mapParsing.getMapList().get(i);
            Location storeLocation = new Location("");
            storeLocation.setLatitude(Double.parseDouble(mapListParsing.getStore_latitude()));
            storeLocation.setLongitude(Double.parseDouble(mapListParsing.getStore_longitude()));
            mapListParsing = new MapListParsing(mapListParsing.getStore_id(), mapListParsing.getStore_name(), mapListParsing.getStore_latitude(), mapListParsing.getStore_longitude(), mapListParsing.getDistance());
            DataList.add(mapListParsing);
        }
        ArrayList<MarkerOptions> markerOptions = new ArrayList<>();
        //마커 여러개 만들기
        for(int i = 0; i< DataList.size(); i++){
            MapListParsing store = DataList.get(i);
            Double lati = Double.parseDouble(store.getStore_latitude());
            Double logi = Double.parseDouble(store.getStore_longitude());
            String name = store.getStore_name();
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(lati, logi)).title(name).snippet(((int)store.getDistance())+"m");
            int height = 110;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.map_marker_purple);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.add(markerOption);
        }
        for (int i = 0 ; i <markerOptions.size(); i++) {
            Marker marker =map.addMarker(markerOptions.get(i));
            marker.setTag(mapParsing.getMapList().get(i).getStore_id()+"");
            marker.showInfoWindow();

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
        mapInfo(mapParsing);
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

    @Override
    protected void onResume() {
        mapFragment.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mapFragment.onPause();
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        mapFragment.onLowMemory();
        super.onLowMemory();
    }

    public void onClickBack(View view) {
    }
}
