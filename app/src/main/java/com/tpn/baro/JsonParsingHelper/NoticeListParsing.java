package com.tpn.baro.JsonParsingHelper;

public class NoticeListParsing {

     String notice_code;
     String notice_date;
     String title;
     int notice_id;
     String content;

    public NoticeListParsing(String notice_code, String notice_date, String title, int notice_id, String content) {
        this.notice_code = notice_code;
        this.notice_date = notice_date;
        this.title = title;
        this.notice_id = notice_id;
        this.content = content;
    }

    public String getNotice_code() {
        return notice_code;
    }

    public void setNotice_code(String notice_code) {
        this.notice_code = notice_code;
    }

    public String getNotice_date() {
        return notice_date;
    }

    public void setNotice_date(String notice_date) {
        this.notice_date = notice_date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(int notice_id) {
        this.notice_id = notice_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
