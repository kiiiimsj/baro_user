package com.example.wantchu.AdapterHelper;

import com.example.wantchu.Inquiry;

public class InquiryData {
    private int inquiry_id;
    private String title;
    private String is_replied;
    private String inquiry_date;

    public int getInquiry_id() {
        return inquiry_id;
    }

    public void setInquiry_id(int inquiry_id) {
        this.inquiry_id = inquiry_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIs_replied() {
        return is_replied;
    }

    public void setIs_replied(String is_replied) {
        this.is_replied = is_replied;
    }

    public String getInquiry_date() {
        return inquiry_date;
    }

    public void setInquiry_date(String inquiry_date) {
        this.inquiry_date = inquiry_date;
    }

    @Override
    public String toString() {
        return "InquiryData{" +
                "inquiry_id=" + inquiry_id +
                ", title='" + title + '\'' +
                ", is_replied='" + is_replied + '\'' +
                ", inquiry_date='" + inquiry_date + '\'' +
                '}';
    }
}
