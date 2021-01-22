package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class ViewPagersListStoreParsing {
    public boolean result;
    public String message;
    public ArrayList<ViewPagerStoreParsing> store;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<ViewPagerStoreParsing> getStore() {
        return store;
    }

    public void setStore(ArrayList<ViewPagerStoreParsing> store) {
        this.store = store;
    }

    public class ViewPagerStoreParsing {
        int store_id;
        String store_info;
        String is_open;
        String store_name;
        String store_image;
        double distance;

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
}
