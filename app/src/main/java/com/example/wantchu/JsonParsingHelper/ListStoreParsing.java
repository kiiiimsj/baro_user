package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class ListStoreParsing {
    boolean result;
    String message;

    ArrayList<ListStoreListParsing> storeLists = new ArrayList<>();

    public ArrayList<ListStoreListParsing> getStoreLists() {
        return storeLists;
    }

    public void setStoreLists(ArrayList<ListStoreListParsing> storeLists) {
        this.storeLists = storeLists;
    }

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
}
