package com.example.wantchu.helperClass;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class myGPSListener implements LocationListener {
    Context context;
    double latitude =0;
    double longitude =0;
    public myGPSListener (Context context) {
        this.context = context;
    }
    public void onLocationChanged(Location location) {
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
        Geocoder geocoder = new Geocoder(context, Locale.KOREA);
        try {
            List<Address> list = geocoder.getFromLocation(latitude, longitude, 10);
            StringBuilder fAddress = new StringBuilder();
            Address address = list.get(0);
            StringBuilder addressBuilder = new StringBuilder();
            addressBuilder.append(address.getCountryName()+" ")
                    .append(address.getAdminArea());
            StringTokenizer stringTokenizer = new StringTokenizer(address.getAddressLine(0), addressBuilder.toString());
            while(stringTokenizer.hasMoreTokens()) {
                fAddress.append(stringTokenizer.nextToken()+" ");
            }
            Log.i("address", fAddress.toString());
            getAddress.setText(fAddress); //--> 처리
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    public LatLng startLocationService(TextView getAdress) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Log.e("err", "???");
        LatLng latLng= null;
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
                    Toast.makeText(context, "GPS를 키고 다시 시도해 주세요", Toast.LENGTH_SHORT).show();
                    latLng = new LatLng(37.4962, 126.9586);
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


