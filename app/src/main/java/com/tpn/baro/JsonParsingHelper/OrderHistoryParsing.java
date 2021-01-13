package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class OrderHistoryParsing {
    public Boolean result;
    public String message;
    public ArrayList<OrderHistoryParsingHelper> order;

    public OrderHistoryParsing(Boolean result, String message, ArrayList<OrderHistoryParsingHelper> order) {
        this.result = result;
        this.message = message;
        this.order = order;
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
}
