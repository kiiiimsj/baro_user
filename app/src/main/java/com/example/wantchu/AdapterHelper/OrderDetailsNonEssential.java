package com.example.wantchu.AdapterHelper;

import java.util.ArrayList;

public class OrderDetailsNonEssential {

    public String ExtraOptionName;
    public String ExtraOption_id;
    public int optionPrice;
    public ArrayList<Integer> justCount;
    private int maxCount;

    public OrderDetailsNonEssential(String extraOptionName, String ExtraOption_id, int optionPrice,int maxCount) {
        ExtraOptionName = extraOptionName;
        this.ExtraOption_id = ExtraOption_id;
        this.optionPrice = optionPrice;
        justCount = new ArrayList<Integer>();
        this.maxCount = maxCount;
    }

    public String getExtraOptionName() {
        return ExtraOptionName;
    }

    public String getExtraOption_id() {
        return ExtraOption_id;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public ArrayList<Integer> getJustCount() {
        return justCount;
    }

    public int getMaxCount() {
        return maxCount;
    }
}
