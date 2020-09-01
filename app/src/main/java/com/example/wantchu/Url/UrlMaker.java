package com.example.wantchu.Url;

public class UrlMaker {
    private String serverUrl = "http://54.180.56.44:8080/";

    public UrlMaker(){}

    public String UrlMake(String lastUrl){
        return serverUrl + lastUrl;
    }

}
