package com.tpn.baro.AdapterHelper;

import java.util.ArrayList;

public class MyPageListbuttons {

    public ArrayList<String> childText;
    public String parentName;

    public MyPageListbuttons(String parentName) {
        childText = new ArrayList<>();
        this.parentName = parentName;
    }
}
