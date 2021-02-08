package com.tpn.baro.JsonParsingHelper;

public class ListStoreListParsing {
    String store_name;
    String store_location;
    String store_image;
    int discount_rate;
    int store_id;
    String is_open;
    float distance;

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


    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    @Override
    public String toString() {
        return "ListStoreListParsing{" +
                "store_name='" + store_name + '\'' +
                ", store_location='" + store_location + '\'' +
                ", store_image='" + store_image + '\'' +
                ", discount_rate=" + discount_rate +
                ", store_id=" + store_id +
                ", is_open='" + is_open + '\'' +
                ", distance=" + distance +
                '}';
    }
}
