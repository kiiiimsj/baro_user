package com.example.wantchu.Database;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.wantchu.HelperDatabase.StoreCategories;

import java.util.ArrayList;
import java.util.HashMap;

public class CategorySessionManager {
    private final static String KEY_CATEGORY = "category";


    SharedPreferences categorySession;
    SharedPreferences.Editor categoryEditor;
    Context context;
    public CategorySessionManager(Context context, String sessionName) {
        this.context = context;
        categorySession = context.getSharedPreferences(sessionName, Context.MODE_PRIVATE);
        categoryEditor = categorySession.edit();
    }
    public SharedPreferences getCategorySession() { return categorySession; }
    public SharedPreferences.Editor getCategoryEditor() { return categoryEditor; }

    public void createCategorySession(ArrayList<StoreCategories> categories) {
        categoryEditor.putString(KEY_CATEGORY, categories.toString());

        categoryEditor.commit();
    }

    public HashMap<String, String> getCategoryFromSession() {
        HashMap<String, String>categoryData = new HashMap<>();
        categoryData.put(KEY_CATEGORY, categorySession.getString(KEY_CATEGORY, null));
        return categoryData;
    }
}
