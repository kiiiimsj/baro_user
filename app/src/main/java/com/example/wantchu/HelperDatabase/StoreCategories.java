package com.example.wantchu.HelperDatabase;

public class StoreCategories {
    private int storeInt;
    private String categoryName;
    private int categoryId;

    public int getStoreInt() {
        return storeInt;
    }

    public void setStoreInt(int storeInt) {
        this.storeInt = storeInt;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public StoreCategories() {}

    public StoreCategories(int storeInt, String categoryName, int categoryId) {
        this.storeInt = storeInt;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
    }
}
