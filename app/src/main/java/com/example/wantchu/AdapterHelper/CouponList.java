package com.example.wantchu.AdapterHelper;

import java.util.ArrayList;

public class CouponList {
    public boolean result;
    public ArrayList<Coupon> coupon;
    public String message;

    public CouponList(ArrayList<Coupon> coupon, boolean result, String message) {
        this.coupon = coupon;
        this.result =result;
        this.message = message;
    }

    @Override
    public String toString() {
        return "CouponList{" +
                "result=" + result +
                ", coupon=" + coupon +
                ", message='" + message + '\'' +
                '}';
    }
}
