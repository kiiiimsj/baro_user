package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class ViewPagersListStoreParsing {
    public boolean result;
    public String message;
    public ArrayList<ViewPagerStoreParsing> store;

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

    public ArrayList<ViewPagerStoreParsing> getStore() {
        return store;
    }

    public void setStore(ArrayList<ViewPagerStoreParsing> store) {
        this.store = store;
    }
}
