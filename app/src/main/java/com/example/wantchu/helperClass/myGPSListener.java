package com.example.wantchu.helperClass;

import android.content.Context;
import android.content.Intent;
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

import com.example.wantchu.MyMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class myGPSListener implements LocationListener {
    Context context;
    double latitude =0;
    double longitude =0;
    SharedPreferences saveLocation;
    public myGPSListener (Context context) {
        this.context = context;
        saveLocation = context.getSharedPreferences("newLocation", Context.MODE_PRIVATE);
    }
    public void onLocationChanged(Location location) {
        if(saveLocation != null ) {
            double[] ll = new double[2];
            int i = 0;
            if(!saveLocation.getString("location", "").equals("")) {
                String locationStr =saveLocation.getString("location", null);
                //Location[gps 37.493879,126.956373 hAcc=??? t=?!? et=?!? vAcc=??? sAcc=??? bAcc=???]
                StringTokenizer stringTokenizer = new StringTokenizer(locationStr, ":");
                while(stringTokenizer.hasMoreTokens()) {
                    String getDouble =stringTokenizer.nextToken();
                    ll[i] =Double.parseDouble(getDouble);
                    i++;
                }
                latitude = ll[0];
                longitude = ll[1];
                String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
                Log.d("Map", message);
                return;
            }
        }
        Log.i("saveLocation", saveLocation.toString());
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
        Log.d("Map", message);
    }

    public void onProviderDisabled(String provider) { }

    public void onProviderEnabled(String provider) { }

    public void onStatusChanged(String provider, int status, Bundle extras) { }

    public LatLng getMyLatLng() {
        LatLng latLng = new LatLng(latitude, longitude);
        if(latitude == 0f || longitude == 0f) {
            startLocationService(null);
        }
        return latLng;
    }
    public void setMapLocationTextView(TextView getAddress) {
        if(getAddress == null) {
            return;
        }
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 10);
            Address address = list.get(0);
            String fAddress = address.getSubLocality()+ " "+address.getThoroughfare();
            getAddress.setText(fAddress); //--> 처리
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setMapLocationTextView(TextView getAddress, LatLng latLng) {
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> list = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10);
            Address address = list.get(0);

            String fAddress = address.getSubLocality()+ " "+address.getThoroughfare();
            getAddress.setText(fAddress); //--> 처리
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public LatLng startLocationService(TextView getAdress) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        LatLng latLng= null;

        if(saveLocation != null ) {
            Log.i("RRRRR", 1+"");
            double[] ll = new double[2];
            int i = 0;
            if(!saveLocation.getString("location", "").equals("")) {
                Log.i("LLLLL", 2+"");
                String locationStr =saveLocation.getString("location", null);
                //Location[gps 37.493879,126.956373 hAcc=??? t=?!? et=?!? vAcc=??? sAcc=??? bAcc=???]
                StringTokenizer stringTokenizer = new StringTokenizer(locationStr, ":");
                while(stringTokenizer.hasMoreTokens()) {
                    String getDouble =stringTokenizer.nextToken();
                    ll[i] =Double.parseDouble(getDouble);
                    i++;
                }
                Log.i("TTTT", 3+"");
                LatLng latLng1 = new LatLng(ll[0],ll[1]);
                latitude = ll[0];
                longitude = ll[1];
                setMapLocationTextView(getAdress);
                Toast.makeText(context.getApplicationContext(), "내 위치확인 요청함",
                        Toast.LENGTH_SHORT).show();
                return latLng1;
            }
        }
        Location location;
        Location location2;
        long minTime = 10000;
        float minDistance = 0;
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);

            location2 = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i("location2" , location2+"");
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i("location", location+"");


            if (location2 != null) {
                Log.e("err", "???");
                latitude = location2.getLatitude();
                longitude = location2.getLongitude();
                latLng = new LatLng(latitude, longitude);
                String message = "가게리스트페이지에서 내 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
                Log.d("Map", message);
            }
            else {
                if(location == null) {
                    Toast.makeText(context, "GPS가 꺼져있으므로 기본위치로 설정합니다.", Toast.LENGTH_SHORT).show();
                    latLng = new LatLng(37.495865, 126.954219);
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
                else {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    latLng = new LatLng(latitude, longitude);
                }
            }
            if(getAdress != null) {
                setMapLocationTextView(getAdress);
                Toast.makeText(context.getApplicationContext(), "내 위치확인 요청함",
                        Toast.LENGTH_SHORT).show();

            }
        }

        catch(SecurityException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}


