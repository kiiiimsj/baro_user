package com.example.wantchu.AdapterHelper;

import com.example.wantchu.Inquiry;

public class InquiryDetailData {
//    "result": true,
//            "inquiry_id": 1,
//            "message": "inquiry_id로 문의 내역 정보 가져오기 성공",
//            "title": "문의 제목 1번 제목",
//            "is_replied": "N",
//            "email": "sky_battle@naver.com",
//            "content": "문의 내용 1번 내용",
//            "inquiry_date": "2020년 8월 20일 "
    private boolean result;
    private int inquiry_id;
    private String message;
    private String title;
    private String is_replied;
    private String email;
    private String content;
    private String inquiry_date;
    public InquiryDetailData() {}
    public InquiryDetailData(boolean result, int inquiry_id, String message, String title, String is_replied, String email, String content, String inquiry_date) {
        this.result = result;
        this.inquiry_id = inquiry_id;
        this.message = message;
        this.title = title;
        this.is_replied = is_replied;
        this.email = email;
        this.content = content;
        this.inquiry_date = inquiry_date;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getInquiry_id() {
        return inquiry_id;
    }

    public void setInquiry_id(int inquiry_id) {
        this.inquiry_id = inquiry_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInquiry_date() {
        return inquiry_date;
    }

    public void setInquiry_date(String inquiry_date) {
        this.inquiry_date = inquiry_date;
    }
}
