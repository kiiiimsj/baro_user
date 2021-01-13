package com.tpn.baro.Url;

public class UrlMaker {
    private String serverUrl = "http://3.35.180.57:8080/";

    public UrlMaker(){}

    public String UrlMake(String lastUrl){
        return serverUrl + lastUrl;
    }

}

