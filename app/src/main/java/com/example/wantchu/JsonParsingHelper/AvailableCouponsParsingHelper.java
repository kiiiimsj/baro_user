package com.example.wantchu.JsonParsingHelper;

public class AvailableCouponsParsingHelper {
    public static final String SALE = "SALE";
    public static final String DISCOUNT = "DISCOUNT";

    private String coupon_title;
    private int coupon_condition;
    private int coupon_id;
    private String coupon_content;
    private String coupon_enddate;
    private int coupon_discount;
    private String coupon_type;

    public String getCoupon_title() {
        return coupon_title;
    }

    public int getCoupon_condition() {
        return coupon_condition;
    }

    public int getCoupon_id() {
        return coupon_id;
    }

    public String getCoupon_content() {
        return coupon_content;
    }

    public String getCoupon_enddate() {
        return coupon_enddate;
    }

    public int getCoupon_discount() {
        return coupon_discount;
    }

    public String getCoupon_type() {
        return coupon_type;
    }
}
