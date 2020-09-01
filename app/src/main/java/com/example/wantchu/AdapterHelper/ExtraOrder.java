package com.example.wantchu.AdapterHelper;

public class ExtraOrder {
    private int extra_id;
    private int extra_price;
    private String extra_name;
    private int extra_count;
    private int extra_maxcount;

    public void setExtra_count(int extra_count) {
        this.extra_count = extra_count;
    }

    public ExtraOrder(int extra_id, int extra_price, String extra_name, int extra_maxcount) {
        this.extra_id = extra_id;
        this.extra_price = extra_price;
        this.extra_name = extra_name;
        this.extra_count = 0;
        this.extra_maxcount = extra_maxcount;
    }

    public int getExtra_id() {
        return extra_id;
    }

    public int getExtra_price() {
        return extra_price;
    }

    public String getExtra_name() {
        return extra_name;
    }

    public int getExtra_count() {
        return extra_count;
    }

    public int getExtra_maxcount() {
        return extra_maxcount;
    }
}
