package com.tpn.baro.JsonParsingHelper;

public class FavoriteListParsing {
    int store_id;
    String store_info;
    String store_latitude;
    String store_longitude;
    String store_name;
    String store_location;
    String store_image;
    String is_open;
    int discount_rate;
    float distance;

    @Override
    public String toString() {
        return "FavoriteListParsing{" +
                "store_id=" + store_id +
                ", store_info='" + store_info + '\'' +
                ", store_latitude='" + store_latitude + '\'' +
                ", store_longitude='" + store_longitude + '\'' +
                ", store_name='" + store_name + '\'' +
                ", store_location='" + store_location + '\'' +
                ", store_image='" + store_image + '\'' +
                ", store_is_open='" + is_open + '\'' +
                ", discount_rate=" + discount_rate +
                ", distance=" + distance +
                '}';
    }

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

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_location() {
        return store_location;
    }

    public void setStore_location(String store_location) {
        this.store_location = store_location;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
