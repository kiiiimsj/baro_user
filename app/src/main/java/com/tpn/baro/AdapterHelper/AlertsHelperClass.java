package com.tpn.baro.AdapterHelper;

import java.util.ArrayList;

public class AlertsHelperClass {
    public boolean result;
    public ArrayList<AlertsHelperClassParsing> alert;
    public String message;

    public AlertsHelperClass() {

    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public ArrayList<AlertsHelperClassParsing> getAlert() {
        return alert;
    }

    public void setAlert(ArrayList<AlertsHelperClassParsing> alert) {
        this.alert = alert;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class AlertsHelperClassParsing {
        public String is_read;
        public String alert_title;
        public String alert_startdate;
        public int alert_id;

        public String getIs_read() {
            return is_read;
        }

        public void setIs_read(String is_read) {
            this.is_read = is_read;
        }

        public String getAlert_title() {
            return alert_title;
        }

        public void setAlert_title(String alert_title) {
            this.alert_title = alert_title;
        }

        public String getAlert_startdate() {
            return alert_startdate;
        }

        public void setAlert_startdate(String alert_startdate) {
            this.alert_startdate = alert_startdate;
        }

        public int getAlert_id() {
            return alert_id;
        }

        public void setAlert_id(int alert_id) {
            this.alert_id = alert_id;
        }

        @Override
        public String toString() {
            return "AlertsHelperClassParsing{" +
                    "is_read='" + is_read + '\'' +
                    ", alert_title='" + alert_title + '\'' +
                    ", alert_startdate='" + alert_startdate + '\'' +
                    ", alert_id=" + alert_id +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AlertsHelperClass{" +
                "result=" + result +
                ", alert=" + alert +
                ", message='" + message + '\'' +
                '}';
    }
}
