package com.tpn.baro.helperClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;

public class MyGPSListener implements LocationListener {
    Context context;
    double latitude =0;
    double longitude =0;
    SharedPreferences saveLocation;
    public MyGPSListener(Context context) {
        this.context = context;
        saveLocation = context.getSharedPreferences("newLocation", Context.MODE_PRIVATE);
    }
    public void onLocationChanged(Location location) {
        if(saveLocation != null ) {
            double[] ll = new double[2];
            int i = 0;
            if(!saveLocation.getString("location", "").equals("")) {
                String locationStr =saveLocation.getString("location", null);
                StringTokenizer stringTokenizer = new StringTokenizer(locationStr, ":");
                while(stringTokenizer.hasMoreTokens()) {
                    String getDouble =stringTokenizer.nextToken();
                    ll[i] =Double.parseDouble(getDouble);
                    i++;
                }
                latitude = ll[0];
                longitude = ll[1];
                String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
                return;
            }
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
    }

    public void onProviderDisabled(String provider) { }

    public void onProviderEnabled(String provider) { }

    public void onStatusChanged(String provider, int status, Bundle extras) { }

    public void setMapLocationTextView(TextView getAddress) {
        if(getAddress == null) {
            return;
        }
        makeRequestForMap(latitude, longitude,getAddress);
    }
    public void makeRequestForMap(double latitude, double longitude, final TextView getAddress) {
        String url = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&coords="+longitude+","+latitude+"&orders=roadaddr&output=json";
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        parsing(response, getAddress);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> header = new HashMap<>();
                header.put("X-NCP-APIGW-API-KEY-ID","5aavwfgus7");
                header.put("X-NCP-APIGW-API-KEY","BkTdUu3zRK8VhD3xqFbVAKorymQxFGIVyZuBDYr0");
                return header;
            }
        };
        requestQueue.add(stringRequest);
    }
    public void parsing(String response, TextView getAddress) {
        try {
            JSONObject getJson = new JSONObject(response);
            JSONObject results = new JSONObject(getJson.getJSONArray("results").getString(0));
            JSONObject region = new JSONObject(results.getString("region"));
            JSONObject area2 = new JSONObject(region.getString("area2"));
            JSONObject area3 = new JSONObject(region.getString("area3"));
            String area2Name = area2.getString("name");
            String area3Name = area3.getString("name");
            getAddress.setText(area2Name + " " + area3Name);
        }
        catch (JSONException e) {

        }
    }
    public void setMapLocationTextView(TextView getAddress, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            Address address = list.get(0);

            String fAddress = address.getSubLocality()+ " "+address.getThoroughfare();
            getAddress.setText(fAddress);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public LatLng startLocationService(TextView getAdress) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        LatLng latLng= null;

        Location gpsProviderLocation;
        Location networkProviderLocation;

        long minTime = 2000;
        float minDistance = 0;

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);

            gpsProviderLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            networkProviderLocation = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            String message = "가게리스트페이지에서 내 위치 -> " + "location: "+ gpsProviderLocation +"\nlocation2:" + networkProviderLocation;
//            Log.e("message_", message);
//            Log.e("location :", saveLocation.getString("location", ""));
            if (gpsProviderLocation != null) {
                latitude = gpsProviderLocation.getLatitude();
                longitude = gpsProviderLocation.getLongitude();
                latLng = new LatLng(latitude, longitude);
            }
            else if (networkProviderLocation != null){
                    latitude = networkProviderLocation.getLatitude();
                    longitude = networkProviderLocation.getLongitude();
                    latLng = new LatLng(latitude, longitude);
            }
//            else if(saveLocation != null || !saveLocation.getString("location", "").equals("")) {
//                double[] ll = new double[2];
//                int i = 0;
//                String locationStr = saveLocation.getString("location", null);
//                StringTokenizer stringTokenizer = new StringTokenizer(locationStr, ":");
//                while (stringTokenizer.hasMoreTokens()) {
//                    String getDouble = stringTokenizer.nextToken();
//                    ll[i] = Double.parseDouble(getDouble);
//                    i++;
//                }
//                Toast.makeText(context, "GPS가 꺼져있으므로 마지막 저장위치를 현재위치로 설정합니다.", Toast.LENGTH_SHORT).show();
//                //LatLng latLng1 = new LatLng(ll[0], ll[1]);
//                latitude = ll[0];
//                longitude = ll[1];
//                setMapLocationTextView(getAdress);
//                latLng = new LatLng(latitude, longitude);
//            }
            else {
                latitude = 37.49808785857802;
                longitude = 127.02758604547965;
                latLng = new LatLng(latitude, longitude);
            }

            if(getAdress != null) {
                setMapLocationTextView(getAdress);
            }
//            Log.e("GPS", latLng+"");
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}


