package com.example.wantchu.HelperDatabase;

import com.google.android.gms.common.util.Strings;

public class StoreDetail {
    private int store_id;
    private String store_opentime;
    private String store_info;
    private double store_latitude;
    private String store_closetime;
    private String store_daysoff;
    private String message;
    private boolean result;
    private String store_phone;
    private double store_longitude;
    private String store_name;
    private String store_location;
    private String type_code;
    private String store_image;
    private String representative_name;
    private String business_number;

    public String getRepresentative_name() {
        return representative_name;
    }

    public String getBusiness_number() {
        return business_number;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getStore_opentime() {
        return store_opentime;
    }

    public void setStore_opentime(String store_opentime) {
        this.store_opentime = store_opentime;
    }

    public String getStore_info() {
        return store_info;
    }

    public void setStore_info(String store_info) {
        this.store_info = store_info;
    }

    public double getStore_latitude() {
        return store_latitude;
    }

    public void setStore_latitude(double store_latitude) {
        this.store_latitude = store_latitude;
    }

    public String getStore_closetime() {
        return store_closetime;
    }

    public void setStore_closetime(String store_closetime) {
        this.store_closetime = store_closetime;
    }

    public String getStore_daysoff() {
        return store_daysoff;
    }

    public void setStore_daysoff(String store_daysoff) {
        this.store_daysoff = store_daysoff;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getStore_phone() {
        return store_phone;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public double getStore_longitude() {
        return store_longitude;
    }

    public void setStore_longitude(double store_longitude) {
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

    public String getType_code() {
        return type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    @Override
    public String toString() {
        return "{" +
                "\"store_id\":" + store_id +
                ",\"store_opentime\":\"" + store_opentime + '\"' +
                ",\"store_info\":\"" + store_info + '\"' +
                ",\"store_latitude\":" + store_latitude +
                ",\"store_closetime\":\"" + store_closetime + '\"' +
                ",\"store_daysoff\":\"" + store_daysoff + '\"' +
                ",\"message\":\"" + message + '\"' +
                ",\"result\":" + result +
                ",\"store_phone\":\"" + store_phone + '\"' +
                ",\"store_longitude\":" + store_longitude +
                ",\"store_name\":\"" + store_name + '\"' +
                ",\"store_location\":\"" + store_location + '\"' +
                ",\"type_code\":\"" + type_code + '\"' +
                ",\"store_image\":\"" + store_image + '\"' +
                '}';
    }
}
