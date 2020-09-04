package com.example.wantchu.AdapterHelper;

import java.util.ArrayList;

public class InquiryDataList {
    public boolean result;
    public ArrayList<InquiryData> inquiry;
    public String message;

    public InquiryDataList(ArrayList<InquiryData> inquiry, boolean result, String message) {
        this.result =result;
        this.inquiry = inquiry;
        this.message = message;
    }

    @Override
    public String toString() {
        return "InquiryDataList{" +
                "result=" + result +
                ", inquiry=" + inquiry +
                ", message='" + message + '\'' +
                '}';
    }
}
