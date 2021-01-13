package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class MapParsing {

    boolean result;
    String message;
    ArrayList<MapListParsing> MapList = new ArrayList<>();
    public MapParsing() {}

    public ArrayList<MapListParsing> getMapList() {
        return MapList;
    }

    public void setMapList(ArrayList<MapListParsing> mapList) {
        MapList = mapList;
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
