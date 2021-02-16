package com.tpn.baro.helperClass;

import com.tpn.baro.AdapterHelper.ExtraOrder;

import java.util.HashMap;

public class DetailsFixToBasket {
    private String name;
    private int menu_id;
    private int count;
    private int defaultPrice;
    private int price;
    private int discount_rate;
    private HashMap<String, ExtraOrder> essentialOptions;
    private HashMap<String, ExtraOrder> nonEssentialoptions;

    public int getMenu_id() {
        return menu_id;
    }

    public void setMenu_id(int menu_id) {
        this.menu_id = menu_id;
    }

    public DetailsFixToBasket(String name, int menu_id, int count, int defaultPrice, int price, int discount_rate, HashMap<String, ExtraOrder> essentialOptions, HashMap<String, ExtraOrder> nonEssentialoptions) {
        this.name = name;
        this.menu_id = menu_id;
        this.count = count;
        this.defaultPrice = defaultPrice;
        this.price = price;
        this.discount_rate = discount_rate;
        this.essentialOptions = essentialOptions;
        this.nonEssentialoptions = nonEssentialoptions;
    }

    public int getDiscount_rate() {
        return discount_rate;
    }

    public void setDiscount_rate(int discount_rate) {
        this.discount_rate = discount_rate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(int defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public HashMap<String, ExtraOrder> getEssentialOptions() {
        return essentialOptions;
    }

    public void setEssentialOptions(HashMap<String, ExtraOrder> essentialOptions) {
        this.essentialOptions = essentialOptions;
    }

    public HashMap<String, ExtraOrder> getNonEssentialoptions() {
        return nonEssentialoptions;
    }

    public void setNonEssentialoptions(HashMap<String, ExtraOrder> nonEssentialoptions) {
        this.nonEssentialoptions = nonEssentialoptions;
    }
}
