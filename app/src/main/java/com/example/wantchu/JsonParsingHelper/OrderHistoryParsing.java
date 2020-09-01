package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class OrderHistoryParsing {
    private Boolean result;
    private String message;
    private ArrayList<OrderHistoryParsingHelper> order;

    public OrderHistoryParsing(Boolean result, String message, ArrayList<OrderHistoryParsingHelper> order) {
        this.result = result;
        this.message = message;
        this.order = order;
    }

    public ArrayList<OrderHistoryParsingHelper> getOrder() {
        return order;
    }
}
