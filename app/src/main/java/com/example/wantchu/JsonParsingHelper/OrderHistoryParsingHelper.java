package com.example.wantchu.JsonParsingHelper;

public class OrderHistoryParsingHelper {
    private String order_date;
    private String store_name;
    private String receipt_id;
    private int total_price;
    private int total_count;

    public OrderHistoryParsingHelper(String order_date, String store_name, String receipt_id, int total_price, int total_count) {
        this.order_date = order_date;
        this.store_name = store_name;
        this.receipt_id = receipt_id;
        this.total_price = total_price;
        this.total_count = total_count;
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


}
