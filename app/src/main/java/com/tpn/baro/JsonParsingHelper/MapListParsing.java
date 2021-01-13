package com.tpn.baro.JsonParsingHelper;

public class MapListParsing implements Comparable<MapListParsing>{

    int store_id;
    String store_name;
    String store_latitude;
    String store_longitude;
    double distance;

    public MapListParsing(int store_id, String store_name, String store_latitude, String store_longitude, double distance) {
        this.store_id = store_id;
        this.store_name = store_name;
        this.store_latitude = store_latitude;
        this.store_longitude = store_longitude;
        this.distance = distance;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public  MapListParsing() {}

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(String store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(String store_longitude) {
        this.store_longitude = store_longitude;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }


    @Override
    public int compareTo(MapListParsing mapListParsing) {
        if(this.distance < mapListParsing.distance){
            return -1;
        }
        else if(this.distance > mapListParsing.distance){
            return 1;
        }
        return 0;
    }
}
