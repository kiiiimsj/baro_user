package com.tpn.baro.JsonParsingHelper;

public class OrderProgressingParsingHelper {
    String order_date;
    String receipt_id;
    String store_name;
    String store_phone;
    int total_price;
    int discount_rate;
    String order_state;
    int total_count;
    String store_image;
    Double store_longitude;
    Double store_latitude;
    int coupon_discount;

    public int getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(int coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setStore_phone(String store_phone) {
        this.store_phone = store_phone;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

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
