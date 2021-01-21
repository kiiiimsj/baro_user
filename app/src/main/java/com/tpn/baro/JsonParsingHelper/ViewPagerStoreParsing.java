package com.tpn.baro.JsonParsingHelper;

public class ViewPagerStoreParsing {
    public int store_id;
    public String store_info;
    public String is_open;
    public String store_name;
    public String store_image;
    public double distance;

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_info() {
        return store_info;
    }

    public void setStore_info(String store_info) {
        this.store_info = store_info;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }
}
