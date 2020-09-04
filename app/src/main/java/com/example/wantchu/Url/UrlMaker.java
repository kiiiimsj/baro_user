package com.example.wantchu.Url;

public class UrlMaker {
    private String serverUrl = "http://15.165.22.64:8080/";

    public UrlMaker(){}

    public String UrlMake(String lastUrl){
        return serverUrl + lastUrl;
    }

}

