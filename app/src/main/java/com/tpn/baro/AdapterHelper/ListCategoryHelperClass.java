package com.tpn.baro.AdapterHelper;

import java.util.ArrayList;

public class ListCategoryHelperClass {
    public ArrayList<String> storeCategoriesName;
    public ArrayList<Integer> storeCategoriesId;
    public String categoryName;
    public int categoryId;

    public ListCategoryHelperClass(String categoryName, int categoryId) {
        storeCategoriesName = new ArrayList<>();
        storeCategoriesId = new ArrayList<>();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }
}
