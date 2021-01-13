package com.tpn.baro.JsonParsingHelper;

import java.util.ArrayList;

public class NoticeParsing {

     boolean result;
     String message;
     ArrayList<NoticeListParsing> NoticeList  = new ArrayList<NoticeListParsing>();
     public void setResult(boolean result) {
          this.result = result;
     }

     public void setMessage(String message) {
          this.message = message;
     }

     public void setNoticeList(ArrayList<NoticeListParsing> noticeList) {
          NoticeList = noticeList;
     }

     public boolean isResult() {
          return result;
     }

     public String getMessage() {
          return message;
     }

     public ArrayList<NoticeListParsing> getNoticeList() {
          return NoticeList;
     }
}
