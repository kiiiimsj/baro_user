package com.example.wantchu.AdapterHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListMenuHelperClass {
    public ArrayList<String> storeMenusName;
    public ArrayList<Integer> storeMenusPrice;
    public ArrayList<Integer> storeMenusId;
    public int menuPrice;
    public String menus;
    public int menuId;

    public ListMenuHelperClass(String menus, int menuPrice, int menuId) {
        this.storeMenusName = new ArrayList<>();
        this.storeMenusPrice = new ArrayList<>();
        this.storeMenusId = new ArrayList<>();
        this.menuPrice = menuPrice;
        this.menus = menus;
        this.menuId = menuId;
    }
}
