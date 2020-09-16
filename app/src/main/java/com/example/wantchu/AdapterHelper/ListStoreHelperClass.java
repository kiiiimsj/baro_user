package com.example.wantchu.AdapterHelper;

import java.util.ArrayList;

public class ListStoreHelperClass{
    public ArrayList<String> storeNames;
    public ArrayList<String> storeLocations;
    public ArrayList<String> storeImages;
    public ArrayList<Float> storeDistances;
    public ArrayList<Integer> storeIds;
    public ArrayList<String> storesIsOpen;
    public String storeName;
    public String storeLocation;
    public String storeImage;
    public float storeDistance;
    public int storeId;
    public String storeIsOpen;
    public ListStoreHelperClass(String storeName, String storeLocation, String storeImage, float storeDistance, int storeId, String storeIsOpen){
        storeNames = new ArrayList<>();
        storeLocations = new ArrayList<>();
        storeImages = new ArrayList<>();
        storeDistances = new ArrayList<>();
        storeIds = new ArrayList<>();
        storesIsOpen = new ArrayList<>();
        this.storeIsOpen =storeIsOpen;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeImage = storeImage;
        this.storeDistance = storeDistance;
        this.storeId = storeId;
    }
    @Override
    public String toString() {
        return "ListStoreHelperClass{" +
                "storeNames=" + storeNames +
                ", storeLocations=" + storeLocations +
                ", storeImages=" + storeImages +
                ", storeDistances=" + storeDistances +
                ", storeIds=" + storeIds +
                ", storeName='" + storeName + '\'' +
                ", storeLocation='" + storeLocation + '\'' +
                ", storeImage='" + storeImage + '\'' +
                ", storeDistance=" + storeDistance +
                ", storeId=" + storeId +
                '}';
    }
}
