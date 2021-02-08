package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class OrderInsertParsing {
    private String phone;
    private int store_id;
    private String receipt_id;
    private int total_price;
    private int discount_price;
    private int coupon_id;
    private String order_date;
    private int each_count;
    int discount_rate;
    private String requests;

    public String getRequest() {
        return requests;
    }

    public void setRequest(String request) {
        this.requests = request;
    }

    public int getEach_count() {
        return each_count;
    }

    public void setEach_count(int each_count) {
        this.each_count = each_count;
    }


    private ArrayList<OrderInsertParsingChild> orders = new ArrayList<>();

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getStore_id() {
        return store_id;
    }

    public void setStore_id(int store_id) {
        this.store_id = store_id;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public int getTotal_price() {
        return total_price;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public int getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(int discount_price) {
        this.discount_price = discount_price;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public ArrayList<OrderInsertParsingChild> getOrders() {
        return orders;
    }

    public void setOrders(ArrayList<OrderInsertParsingChild> orders) {
        this.orders = orders;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public String getRequests() {
        return requests;
    }

    public void setRequests(String requests) {
        this.requests = requests;
    }
}
