package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class OrderInsertParsingChild {
    private int menu_id;
    private String menu_name;
    private int menu_defaultprice;
    private int order_count;
    private ArrayList<OrderInsertParsingChild2> extras =new ArrayList<>();


    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public void setMenu_defaultprice(int menu_defaultprice) {
        this.menu_defaultprice = menu_defaultprice;
    }

    public void setOrder_count(int order_count) {
        this.order_count = order_count;
    }

    public void setExtras(ArrayList<OrderInsertParsingChild2> extras) {
        this.extras = extras;
    }

    public int getMenu_id() {
        return menu_id;
    }

    public int getMenu_defaultprice() {
        return menu_defaultprice;
    }

    public int getOrder_count() {
        return order_count;
    }

    public ArrayList<OrderInsertParsingChild2> getExtras() {
        return extras;
    }

}