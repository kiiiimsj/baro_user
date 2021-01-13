package com.tpn.baro.AdapterHelper;

import java.util.ArrayList;

public class FavoriteHelperClass implements Comparable<FavoriteHelperClass> {

    public ArrayList<String> storeIds;
    public ArrayList<String> storeInfos;
    public ArrayList<String> storeLatitudes;
    public ArrayList<String> storeLongitudes;
    public ArrayList<String> storeNames;
    public ArrayList<String> storeLocations;
    public ArrayList<String> storeImages;
    public ArrayList<Double> storeDistances;
    public String storeId;
    public String storeInfo;
    public String storeLatitude;
    public String storeLongitude;
    public String storeName;
    public String storeLocation;
    public String storeImage;
    public double storeDistance;

    public FavoriteHelperClass(String storeId, String storeInfo, String storeLatitude, String storeLongitude, String storeName, String storeLocation, String storeImage, double storeDistance) {
        this.storeId = storeId;
        this.storeInfo = storeInfo;
        this.storeLatitude = storeLatitude;
        this.storeLongitude = storeLongitude;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeImage = storeImage;
        this.storeDistance = storeDistance;
    }

    @Override
    public int compareTo(FavoriteHelperClass favoriteHelperClass) {
        if(this.storeDistance < favoriteHelperClass.storeDistance){
            return -1;
        }
        else if(this.storeDistance > favoriteHelperClass.storeDistance){
            return 1;
        }
        return 0;
    }
}



//
//"store_id": 1,
//        "store_info": "안녕하세요 이 카페는 테스트용 카페입니다.",
//        "store_latitude": 37.4952,
//        "store_longitude": 126.9565,
//        "store_name": "test cafe",
//        "store_location": "서울특별시 테스트구 테스트동 테스트로 111 테스트빌딩 2층",
//        "store_image": "default.png"