package com.example.wantchu.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wantchu.HelperDatabase.StoreDetail;

import java.util.ArrayList;
import java.util.HashMap;

public class StoreSessionManager {
    SharedPreferences storesSession;
    SharedPreferences.Editor storeEditor;
    Context context;

    public static final String STORE_SESSION = "storeSession";

    public StoreSessionManager(Context context, String sessionName) {
        this.context = context;
        storesSession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        storeEditor = storesSession.edit();
    }
    //Store session variables
    private static final String KEY_STORE_ID = "storeId";
    private static final String KEY_STORE_INFO = "storeInfo";
    private static final String KEY_STORE_LATITUDE ="storeLatitude";
    private static final String KEY_STORE_CLOSE_TIME = "storeCloseTime";
    private static final String KEY_STORE_DAYS_OFF ="storeDaysoff";
    private static final String KEY_STORE_MESSAGE = "message";
    private static final String KEY_STORE_RESULT = "result";
    private static final String KEY_STORE_PHONE ="storePhone";
    private static final String KEY_STORE_LONGITUDE = "storeLongitude";
    private static final String KEY_STORE_NAME = "name";
    private static final String KEY_STORE_LOCATION ="storeLocation";
    private static final String KEY_STORE_TYPE_CODE ="typeCode";


    private static final String KEY_STORES = "stores";

    public static final String IS_FAVORITE = "isFavorite";

    public SharedPreferences.Editor getStoreEditor() {
return storeEditor;
    }
    public SharedPreferences getStoresSession() {return storesSession;}
    public void createStoreSession(String storeId, String storeInfo, String storeLatitude, String storeCloseTime, String storeDaysOff, String message,
                                   String storePhone, String storeLongitude, String name, String storeLocation, String typeCode) {
        storeEditor.putString(KEY_STORE_ID, storeId);
        storeEditor.putString(KEY_STORE_INFO, storeInfo);
        storeEditor.putString(KEY_STORE_LATITUDE, storeLatitude);
        storeEditor.putString(KEY_STORE_CLOSE_TIME, storeCloseTime);
        storeEditor.putString(KEY_STORE_DAYS_OFF, storeDaysOff);
        storeEditor.putString(KEY_STORE_MESSAGE, message);
        storeEditor.putBoolean(KEY_STORE_RESULT, true);
        storeEditor.putString(KEY_STORE_PHONE, storePhone);
        storeEditor.putString(KEY_STORE_LONGITUDE, storeLongitude);
        storeEditor.putString(KEY_STORE_NAME, name);
        storeEditor.putString(KEY_STORE_LOCATION, storeLocation);
        storeEditor.putString(KEY_STORE_TYPE_CODE, typeCode);
        storeEditor.commit();
    }
    public void createStoreSession(ArrayList<StoreDetail> storeDetails, boolean result) {
        ArrayList<StoreDetail> stores = new ArrayList<>();
        storeEditor.putString(KEY_STORES, storeDetails.toString());
        storeEditor.putBoolean(IS_FAVORITE, result);

        storeEditor.commit();
    }
    public boolean getIsFavorite() {
        if(storesSession.getBoolean(IS_FAVORITE, false)) {
            return true;
        }
        return false;
    }
    public void setIsFavorite(boolean result) {
        storeEditor.putBoolean(IS_FAVORITE, result);

        storeEditor.commit();
    }

    public HashMap<String, String> getStoreDetailFromSession() {
        HashMap<String, String> storeData = new HashMap<>();

        storeData.put(KEY_STORE_ID, storesSession.getString(KEY_STORE_ID, null));
        storeData.put(KEY_STORE_INFO, storesSession.getString(KEY_STORE_INFO, null));
        storeData.put(KEY_STORE_LATITUDE, storesSession.getString(KEY_STORE_LATITUDE, null));
        storeData.put(KEY_STORE_CLOSE_TIME, storesSession.getString(KEY_STORE_CLOSE_TIME, null));
        storeData.put(KEY_STORE_DAYS_OFF, storesSession.getString(KEY_STORE_DAYS_OFF, null));
        storeData.put(KEY_STORE_MESSAGE, storesSession.getString(KEY_STORE_MESSAGE, null));
        storeData.put(KEY_STORE_RESULT, storesSession.getString(KEY_STORE_RESULT, null));
        storeData.put(KEY_STORE_PHONE, storesSession.getString(KEY_STORE_PHONE, null));
        storeData.put(KEY_STORE_LONGITUDE, storesSession.getString(KEY_STORE_LONGITUDE, null));
        storeData.put(KEY_STORE_NAME, storesSession.getString(KEY_STORE_NAME, null));
        storeData.put(KEY_STORE_LOCATION, storesSession.getString(KEY_STORE_LOCATION, null));
        storeData.put(KEY_STORE_TYPE_CODE, storesSession.getString(KEY_STORE_TYPE_CODE, null));

        return storeData;
    }

    public HashMap<String, String> getStoreDetailFromSessionById(int storeId) {
        HashMap<String, String> storeData = getStoreDetailFromSession();
        storeData.containsValue(storeId);

        return storeData;
    }
}
