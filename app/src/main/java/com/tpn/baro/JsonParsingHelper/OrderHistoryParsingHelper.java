package com.tpn.baro.JsonParsingHelper;

import java.text.SimpleDateFormat;

public class OrderHistoryParsingHelper implements Comparable<OrderHistoryParsingHelper>{
    private String order_date;
    private String store_name;
    private String receipt_id;
    private int total_price;
    private int total_count;
    private int discount_rate;
    private String order_state;
    private String store_id;
    private String store_image;

    public String getOrder_state() {
        return order_state;
    }

    public OrderHistoryParsingHelper(String order_date, String store_name, String receipt_id, int total_price, int total_count, int discount_rate, String order_state, String store_id, String store_image) {
        this.order_date = order_date;
        this.store_name = store_name;
        this.receipt_id = receipt_id;
        this.total_price = total_price;
        this.total_count = total_count;
        this.discount_rate = discount_rate;
        this.order_state = order_state;
        this.store_id = store_id;
        this.store_image = store_image;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public void setReceipt_id(String receipt_id) {
        this.receipt_id = receipt_id;
    }

    public void setTotal_price(int total_price) {
        this.total_price = total_price;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
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

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public String getOrder_date() {
        return order_date;
    }

    public String getStore_name() {
        return store_name;
    }

    public String getReceipt_id() {
        return receipt_id;
    }

    public int getTotal_count() {
        return total_count;
    }

    public int getTotal_price() {
        return total_price;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    @Override
    public int compareTo(OrderHistoryParsingHelper orderHistoryParsingHelper) {
        String thisDate = new SimpleDateFormat("yyyyMMdd").format(this.order_date);
        String getDate = new SimpleDateFormat("yyyyMMdd").format(orderHistoryParsingHelper.order_date);
        if(Integer.parseInt(thisDate) < Integer.parseInt(getDate)){
            return -1;
        }
        else if(Integer.parseInt(this.order_date) > Integer.parseInt(orderHistoryParsingHelper.order_date)){
            return 1;
        }
        return 0;
    }
}
