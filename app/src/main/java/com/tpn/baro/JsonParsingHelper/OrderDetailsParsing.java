package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class OrderDetailsParsing {

    String result;
    ArrayList<OrderDetailsListParsing> extraList = new ArrayList<>();
    String message;



    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ArrayList<OrderDetailsListParsing> getExtraList() {
        return extraList;
    }

    public void setExtraList(ArrayList<OrderDetailsListParsing> extraList) {
        this.extraList = extraList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
