package com.example.wantchu.AdapterHelper;

public class Coupon {
    private String coupon_title;
    private String coupon_condition;
    private int coupon_id;
    private String coupon_content;
    private String coupon_enddate;
    private String coupon_discount;
    private String coupon_type;

    public Coupon() { }

    public Coupon(String coupon_title, String coupon_condition, int coupon_id, String coupon_content, String coupon_endDate, String coupon_discount, String coupon_type) {
        this.coupon_title = coupon_title;
        this.coupon_condition = coupon_condition;
        this.coupon_id = coupon_id;
        this.coupon_content = coupon_content;
        this.coupon_enddate = coupon_endDate;
        this.coupon_discount = coupon_discount;
        this.coupon_type = coupon_type;
    }

    public String getCoupon_title() {
        return coupon_title;
    }

    public void setCoupon_title(String coupon_title) {
        this.coupon_title = coupon_title;
    }

    public String getCoupon_condition() {
        return coupon_condition;
    }

    public void setCoupon_condition(String coupon_condition) {
        this.coupon_condition = coupon_condition;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public void setCoupon_id(int coupon_id) {
        this.coupon_id = coupon_id;
    }

    public String getCoupon_content() {
        return coupon_content;
    }

    public void setCoupon_content(String coupon_content) {
        this.coupon_content = coupon_content;
    }

    public String getCoupon_enddate() {
        return coupon_enddate;
    }

    public void setCoupon_enddate(String coupon_enddate) {
        this.coupon_enddate = coupon_enddate;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public String getCoupon_type() {
        return coupon_type;
    }

    public void setCoupon_type(String coupon_type) {
        this.coupon_type = coupon_type;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "coupon_title='" + coupon_title + '\'' +
                ", coupon_condition='" + coupon_condition + '\'' +
                ", coupon_id=" + coupon_id +
                ", coupon_content='" + coupon_content + '\'' +
                ", coupon_enddate='" + coupon_enddate + '\'' +
                ", coupon_discount='" + coupon_discount + '\'' +
                ", coupon_type='" + coupon_type + '\'' +
                '}';
    }
}

