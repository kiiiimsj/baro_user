package com.example.wantchu.JsonParsingHelper;

public class OrderProgressingParsingHelper {
    String order_date;
    String receipt_id;
    String store_name;
    String store_phone;
    int total_price;
    String order_state;
    int total_count;
    String store_image;
    Double store_longitude;
    Double store_latitude;


    public String getOrder_date() {
        return order_date;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public String getStore_name() {
        return store_name;
    }

    public int getTotal_price() {
        return total_price;
    }

    public String getOrder_state() {
        return order_state;
    }

    public int getTotal_count() {
        return total_count;
    }

    public String getStore_image() {
        return store_image;
    }

    public Double getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(Double store_longitude) {
        this.store_longitude = store_longitude;
    }

    public Double getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(Double store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getStore_phone() {
        return store_phone;
    }
}
