package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class OrderDetailsListParsing {

    String extra_group;
    int extra_id;
    int extra_price;
    String extra_name;
    int extra_maxcount;

    public OrderDetailsListParsing(String extra_group, int extra_id, int extra_price, String extra_name, int extra_maxcount) {
        this.extra_group = extra_group;
        this.extra_id = extra_id;
        this.extra_price = extra_price;
        this.extra_name = extra_name;
        this.extra_maxcount = extra_maxcount;
    }

    public String getExtra_group() {
        return extra_group;
    }

    public void setExtra_group(String extra_group) {
        this.extra_group = extra_group;
    }

    public int getExtra_id() {
        return extra_id;
    }

    public void setExtra_id(int extra_id) {
        this.extra_id = extra_id;
    }

    public int getExtra_price() {
        return extra_price;
    }

    public void setExtra_price(int extra_price) {
        this.extra_price = extra_price;
    }

    public String getExtra_name() {
        return extra_name;
    }

    public void setExtra_name(String extra_name) {
        this.extra_name = extra_name;
    }
    public int getExtra_maxcount() {
        return extra_maxcount;
    }

    public void setExtra_maxcount(int extra_maxcount) {
        this.extra_maxcount = extra_maxcount;
    }

}
