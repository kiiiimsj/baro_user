package com.example.wantchu.JsonParsingHelper;

public class AlertIsNewParsing {
    boolean result;
    int recentlyAlertId;
    String message;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public int getRecentlyAlertId() {
        return recentlyAlertId;
    }

    public void setRecentlyAlertId(int recentlyAlertId) {
        this.recentlyAlertId = recentlyAlertId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AlertIsNewParsing{" +
                "result=" + result +
                ", recentlyAlertId=" + recentlyAlertId +
                ", message='" + message + '\'' +
                '}';
    }
}
