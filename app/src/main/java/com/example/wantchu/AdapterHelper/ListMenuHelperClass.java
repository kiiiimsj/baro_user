package com.example.wantchu.AdapterHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListMenuHelperClass {
    public ArrayList<String> storeMenusName;
    public ArrayList<Integer> storeMenusPrice;
    public ArrayList<Integer> storeMenusId;
    public ArrayList<String> menuImages;
    public int menuPrice;
    public String menus;
    public int menuId;
    public String menuImage;
    public String menu_info;

    public ListMenuHelperClass(String menus, int menuPrice, int menuId, String menuImage,String menu_info) {
        this.storeMenusName = new ArrayList<>();
        this.storeMenusPrice = new ArrayList<>();
        this.storeMenusId = new ArrayList<>();
        this.menuImages = new ArrayList<>();
        this.menuPrice = menuPrice;
        this.menus = menus;
        this.menuId = menuId;
        this.menuImage = menuImage;
        this.menu_info = menu_info;
    }
}
