package com.tpn.baro;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tpn.baro.Dialogs.MapSetPositionDialog;
import com.tpn.baro.Fragment.TopBar;
import com.tpn.baro.HelperDatabase.StoreDetail;
import com.tpn.baro.JsonParsingHelper.MapListParsing;
import com.tpn.baro.JsonParsingHelper.MapParsing;
import com.tpn.baro.Url.UrlMaker;
import com.tpn.baro.helperClass.MyGPSListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MyMap extends AppCompatActivity implements AutoPermissionsListener, MapSetPositionDialog.isClickOkay, TopBar.OnBackPressedInParentActivity, TopBar.ClickButton {
    SupportMapFragment mapFragment;
    GoogleMap map;
    MarkerOptions oldMarkerOption;

    Button setNewLatLng;
    RelativeLayout OverLayMarker;

    Marker newMarker;

    LatLng oldLatLng;

    TextView address;
    RelativeLayout storeDetail;
    TextView storeTitle;
    ImageView storePreview;
    TextView distance;
    TextView storeAddress;
    StoreDetail storeDetailData;


    MapSetPositionDialog mapSetPositionDialog;
    SharedPreferences setMyNewLocation;
    MyGPSListener GPSListener;
    SharedPreferences.Editor editor;


    FragmentManager fm;
    TopBar topBar;

    boolean firstRedMarker = true;

    Button.OnClickListener clickListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_map);
        setMyNewLocation = getSharedPreferences("newLocation", MODE_PRIVATE);
        address = findViewById(R.id.show_latlng_address);
        OverLayMarker = findViewById(R.id.over_lay_marker);
        setNewLatLng = findViewById(R.id.set_location);
        storeDetail = findViewById(R.id.store_detail);

        storeTitle = findViewById(R.id.store_title);
        storePreview = findViewById(R.id.store_preview);
        distance = findViewById(R.id.distance);
        storeAddress = findViewById(R.id.address);

        GPSListener = new MyGPSListener(MyMap.this);
        mapSetPositionDialog = new MapSetPositionDialog(this, this);

        makeRequest(setHashMapData());
        editor = setMyNewLocation.edit();
        storeDetail.setVisibility(View.INVISIBLE);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        setMapFragmentGetMapAsync();

        fm = getSupportFragmentManager();
        topBar = (TopBar) fm.findFragmentById(R.id.top_bar);
        AutoPermissions.Companion.loadAllPermissions(this, 101);
    }

    private void setMapFragmentGetMapAsync() {
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
                oldMarkerOption = new MarkerOptions();
                oldLatLng = GPSListener.startLocationService(null);
                OverLayMarker.setVisibility(View.INVISIBLE);
                setNewLatLng.setVisibility(View.GONE);
                GPSListener.setMapLocationTextView(address, oldLatLng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(oldLatLng, 15));
                map.getUiSettings().setMapToolbarEnabled(false);
                if(!firstRedMarker){
                    oldMarkerOption.position(oldLatLng);
                    map.addMarker(oldMarkerOption);
                }
                try {
                    map.moveCamera(CameraUpdateFactory.newLatLng(oldLatLng));
                    map.setContentDescription("현재 설정 위치");
                    map.setMyLocationEnabled(true);
                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            OverLayMarker.setVisibility(View.INVISIBLE);
                            if(marker.getTag() == null) {
                                storeDetail.setVisibility(View.INVISIBLE);
                                return false;
                            }
                            storeDetail.setVisibility(View.VISIBLE);
                            makeRequestForStoreDetail(marker.getTag()+"");
                            return true;
                        }
                    });
                    clickListener = new Button.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            storeDetail.setVisibility(View.INVISIBLE);
                            setNewLatLng.setVisibility(View.VISIBLE);
                            OverLayMarker.setVisibility(View.VISIBLE);
                            final LatLng latLng = new LatLng(map.getCameraPosition().target.latitude, map.getCameraPosition().target.longitude);
                            final MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                            markerOptions.visible(false);

                            GPSListener.setMapLocationTextView(address, latLng);
                            map.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                                @Override
                                public void onCameraIdle() {
                                    storeDetail.setVisibility(View.INVISIBLE);
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
                                    newMarker = map.addMarker(markerOptions);
                                }
                            });
                        }
                    };
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

    private void makeRequestForStoreDetail(String storeInfo) {
        final StringTokenizer stringTokenizer = new StringTokenizer(storeInfo, ":");
        String id =stringTokenizer.nextToken();
        String url = new UrlMaker().UrlMake("StoreFindById.do?store_id=" + id);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                storeParsing(response, stringTokenizer.nextToken());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void storeParsing(String response, String distance) {
        Gson gson = new GsonBuilder().create();
        storeDetailData = gson.fromJson(response, StoreDetail.class);
        makeRequestForImage(distance);

    }

    public void makeRequestForImage(final String distanceStr) {
        String url = new UrlMaker().UrlMake("ImageStore.do?image_name="+storeDetailData.getStore_image());
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

    private void setStoreInfo(String distanceStr, Bitmap response) {
        storePreview.setImageBitmap(response);
        storeTitle.setText(storeDetailData.getStore_name());
        storeAddress.setText(storeDetailData.getStore_location());
        distance.setText(((int)Double.parseDouble(distanceStr))+"m");
        storeDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(MyMap.this, StoreInfoReNewer.class);
                    intent.putExtra("store_id", storeDetailData.getStore_id()+"");
                    startActivity(intent);
            }
        });
        fm.beginTransaction().show(topBar);
    }

    @Override
    public void clickOkay() {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(newMarker.getPosition().latitude);
        temp.setLongitude(newMarker.getPosition().longitude);
        editor.putString("location", ":"+newMarker.getPosition().latitude+":"+newMarker.getPosition().longitude);
        editor.apply();
        editor.commit();
        map.clear();
        makeRequest(setHashMapData());
        firstRedMarker = false;
        setMapFragmentGetMapAsync();
        OverLayMarker.setVisibility(View.INVISIBLE);
        setNewLatLng.setVisibility(View.GONE);
    }

    @Override
    public void clickCancel() {
        OverLayMarker.setVisibility(View.INVISIBLE);
        setNewLatLng.setVisibility(View.GONE);
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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(data), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
            Double logi = Double.parseDouble(store.getStore_longitude());
            Double lati = Double.parseDouble(store.getStore_latitude());
            String name = store.getStore_name();
            MarkerOptions markerOption = new MarkerOptions().position(new LatLng(lati, logi)).title(name).snippet(((int)store.getDistance())+"m");
            int height = 110;
            int width = 80;
            BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.store_marker);
            Bitmap b = bitmapdraw.getBitmap();
            Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
            markerOption.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
            markerOptions.add(markerOption);
        }
        for (int i = 0 ; i <markerOptions.size(); i++) {
            Marker marker =map.addMarker(markerOptions.get(i));
            marker.setTag(mapParsing.getMapList().get(i).getStore_id()+":"+mapParsing.getMapList().get(i).getDistance());
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
    @Override
    public void onBack() {
        super.onBackPressed();
    }

    @Override
    public void clickButton() {
        clickListener.onClick(null);
    }
}
