package com.example.wantchu.JsonParsingHelper;

public class ListStoreListParsing {
    String store_name;
    String store_location;
    String store_image;
    int store_id;
    String is_open;
    float distance;

    public ListStoreListParsing(String store_name, String store_location, String store_image, int store_id, String isOpen, float distance) {
        this.store_name = store_name;
        this.store_location = store_location;
        this.store_image = store_image;
        this.store_id = store_id;
        this.is_open = isOpen;
        this.distance = distance;
    }
    public ListStoreListParsing(String store_name, String store_location, String store_image, int store_id){
        this.store_name = store_name;
        this.store_location = store_location;
        this.store_image = store_image;
        this.store_id = store_id;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getStore_location() {
        return store_location;
    }

    public String getStore_image() {
        return store_image;
    }

    public int getStore_id() { return store_id; }

    public String getIs_open() {
        return is_open;
    }

    public void setIs_open(String is_open) {
        this.is_open = is_open;
    }

    @Override
    public String toString() {
        return "ListStoreListParsing{" +
                "store_name='" + store_name + '\'' +
                ", store_location='" + store_location + '\'' +
                ", store_image='" + store_image + '\'' +
                ", store_id=" + store_id +
                ", is_open='" + is_open + '\'' +
                ", distance=" + distance +
                '}';
    }
}
