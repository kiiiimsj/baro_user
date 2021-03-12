package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class OrderHistoryParsing {
    public Boolean result;
    public String message;
    public ArrayList<OrderHistoryParsingHelper> order;
    int discount_rate;

    public void setOrder(ArrayList<OrderHistoryParsingHelper> order) {
        this.order = order;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }
    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<OrderHistoryParsingHelper> getOrder() {
        return order;
    }

    public OrderHistoryParsing(Boolean result, String message, ArrayList<OrderHistoryParsingHelper> order, int discount_rate) {
        this.result = result;
        this.message = message;
        this.order = order;
        this.discount_rate = discount_rate;
    }
}
