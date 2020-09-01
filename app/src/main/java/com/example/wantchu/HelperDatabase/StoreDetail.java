package com.example.wantchu.HelperDatabase;

import com.google.android.gms.common.util.Strings;

public class StoreDetail {
    private int storeId;
    private String storeOpenTime;
    private String storeInfo;
    private double storeLatitude;
    private String storeCloseTime;
    private String storeDaysoff;
    private String message;
    private boolean result;
    private String storePhone;
    private double storeLongitude;
    private String name;
    private String storeLocation;
    private String typeCode;
    private String store_image;


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreOpenTime() {
        return storeOpenTime;
    }

    public void setStoreOpenTime(String storeOpenTime) {
        this.storeOpenTime = storeOpenTime;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public double getStoreLatitude() {
        return storeLatitude;
    }

    public void setStoreLatitude(double storeLatitude) {
        this.storeLatitude = storeLatitude;
    }

    public String getStoreCloseTime() {
        return storeCloseTime;
    }

    public void setStoreCloseTime(String storeCloseTime) {
        this.storeCloseTime = storeCloseTime;
    }

    public String getStoreDaysoff() {
        return storeDaysoff;
    }

    public void setStoreDaysoff(String storeDaysoff) {
        this.storeDaysoff = storeDaysoff;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public double getStoreLongitude() {
        return storeLongitude;
    }

    public void setStoreLongitude(double storeLongitude) {
        this.storeLongitude = storeLongitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    public String getStore_image() {
        return store_image;
    }

    public void setStore_image(String store_image) {
        this.store_image = store_image;
    }

    public StoreDetail() {}

    public StoreDetail(int storeId, String storeOpenTime, String storeInfo, double storeLatitude, String storeCloseTime, String storeDaysoff,
                       String message, boolean result, String storePhone, double storeLongitude, String name, String storeLocation, String typeCode, String storeImage) {
        this.storeId = storeId;
        this.storeOpenTime = storeOpenTime;
        this.storeInfo = storeInfo;
        this.storeLatitude = storeLatitude;
        this.storeCloseTime = storeCloseTime;
        this.storeDaysoff = storeDaysoff;
        this.message = message;
        this.result = result;
        this.storePhone = storePhone;
        this.storeLongitude = storeLongitude;
        this.name = name;
        this.storeLocation = storeLocation;
        this.typeCode = typeCode;
        this.store_image = storeImage;
    }

    @Override
    public String toString() {
        //{"result":true,"message":"01093756927의즐겨찾기 정보 가져오기 성공","favorite":[{"store_id":1,"store_info":"안녕하세요 이 카페는 테스트용 카페입니다.","store_latitude":37.4952,"store_longitude":126.9565,"store_name":"test cafe","store_location":"서울특별시 테스트구 테스트동 테스트로 111 테스트빌딩 2층","store_image":"test_cafe1.png"},{"store_id":3,"store_info":"CAFE2 의 정보 입니다.","store_latitude":0.1,"store_longitude":0.1,"store_name":"TEST CAFE2","store_location":"테스트시 테스트동","store_image":"test_cafe2.png"},{"store_id":4,"store_info":"CAFE3 의 정보 입니다.","store_latitude":0.2,"store_longitude":0.2,"store_name":"TEST CAFE3","store_location":"테스트시 테스트2동","store_image":"test_cafe3.png"},{"store_id":15,"store_info":"자연별곡 개노맛","store_latitude":3.1,"store_longitude":3.1,"store_name":"자연별곡","store_location":"테스트시 테스트10동","store_image":"default.png"},{"store_id":19,"store_info":"존나 맛있는 샌드위치 에그드랍","store_latitude":10.1,"store_longitude":10.2,"store_name":"에그드랍","store_location":"서울 중구 퇴계로 18 2층 2-6호","store_image":"default.png"}]}
        return "{" +
                "\"storeId\":" + storeId +
                ",\"storeOpenTime\":\"" + storeOpenTime + '\"' +
                ",\"storeInfo\":\"" + storeInfo + '\"' +
                ",\"storeLatitude\":" + storeLatitude +
                ",\"storeCloseTime\":\"" + storeCloseTime + '\"' +
                ",\"storeDaysoff\":\"" + storeDaysoff + '\"' +
                ",\"message\":\"" + message + '\"' +
                ",\"result\":" + result +
                ",\"storePhone\":\"" + storePhone + '\"' +
                ",\"storeLongitude\":" + storeLongitude +
                ",\"name\":\"" + name + '\"' +
                ",\"storeLocation\":\"" + storeLocation + '\"' +
                ",\"typeCode\":\"" + typeCode + '\"' +
                ",\"storeImage\":\"" + store_image + '\"' +
                '}';
    }
}
