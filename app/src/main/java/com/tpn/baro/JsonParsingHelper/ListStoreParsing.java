package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class ListStoreParsing {
    public boolean result;
    public String message;
    public ArrayList<ListStoreListParsing> store;
    public ArrayList<ListStoreListParsing> getStoreLists() {
        return store;
    }
    public void setStoreLists(ArrayList<ListStoreListParsing> store) {
        this.store = store;
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
