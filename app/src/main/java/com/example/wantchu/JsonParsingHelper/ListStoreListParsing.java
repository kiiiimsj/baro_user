package com.example.wantchu.JsonParsingHelper;

public class ListStoreListParsing {
    String storeName;
    String storeLatitude;
    String storeLongitude;
    String storeLocation;
    String storeImage;
    int storeId;
    String storeIsOpen;

    public ListStoreListParsing(String storeName, String storeLatitude, String storeLongitude, String storeLocation, String storeImage, int storeId, String isOpen) {
        this.storeName = storeName;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeLocation = storeLocation;
        this.storeImage = storeImage;
        this.storeId = storeId;
        this.storeIsOpen = isOpen;
    }
    public ListStoreListParsing(String storeName, String storeLatitude, String storeLongitude, String storeLocation, String storeImage, int storeId){
        this.storeName = storeName;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeLocation = storeLocation;
        this.storeImage = storeImage;
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public String getStoreImage() {
        return storeImage;
    }

    public String getStoreLatitude() {
        return storeLatitude;
    }

    public String getStoreLongitude() {
        return storeLongitude;
    }

    public int getStoreId() { return storeId; }

    public String getStoreIsOpen() {
        return storeIsOpen;
    }

    public void setStoreIsOpen(String storeIsOpen) {
        this.storeIsOpen = storeIsOpen;
    }

    @Override
    public String toString() {
        return "{" +
                "\"storeName\":\"" + storeName + '\"' +
                ",\"storeLatitude\":\"" + storeLatitude + '\"' +
                ",\"storeLongitude\":\"" + storeLongitude + '\"' +
                ",\"storeLocation\":\"" + storeLocation + '\"' +
                ",\"storeImage\":\"" + storeImage + '\"' +
                ",\"storeId\":" + storeId +
                ",\"isOpen\":\"" + storeIsOpen + '\"' +
                '}';
    }
}
