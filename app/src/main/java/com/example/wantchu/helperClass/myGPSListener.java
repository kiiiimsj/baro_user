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

public class myGPSListener implements LocationListener {
    Context context;
    public myGPSListener (Context context) {
        this.context = context;
    }
    public void onLocationChanged(Location location) {
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();

        String message = "내 위치 -> Latitude : "+ latitude + "\nLongitude:"+ longitude;
        Log.d("Map", message);


    }

    public void onProviderDisabled(String provider) { }

    public void onProviderEnabled(String provider) { }

    public void onStatusChanged(String provider, int status, Bundle extras) { }

    public LatLng startLocationService(TextView getAdress) {
        LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        Log.e("err", "???");
        LatLng latLng= null;
        Location location;
        long minTime = 10000;
        float minDistance = 0;

        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, this);
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, this);

            location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            double latitude;
            double longitude;
            if (location != null) {
                Log.e("err", "???");
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                latLng = new LatLng(latitude, longitude);
                String message = "가게리스트페이지에서 내 위치 -> Latitude : " + latitude + "\nLongitude:" + longitude;
                Log.d("Map", message);
            }
            else {
                latitude = 37.4963;
                longitude = 126.9575;
                latLng = new LatLng(latitude, longitude);
            }

            Geocoder geocoder = new Geocoder(context, Locale.KOREA);
            try {
                List<Address> list = geocoder.getFromLocation(latitude, longitude, 10);
                Log.d("MyMapaa", list.get(0).getAddressLine(0));
                getAdress.setText(list.get(0).getAddressLine(0)); //--> 처리
            }
            catch(Exception e){
                e.printStackTrace();
            }

            Toast.makeText(context.getApplicationContext(), "내 위치확인 요청함",
                    Toast.LENGTH_SHORT).show();

        }

        catch(SecurityException e) {
            e.printStackTrace();
        }
        return latLng;
    }
}


