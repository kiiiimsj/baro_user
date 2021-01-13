package com.example.wantchu.AdapterHelper;

import com.example.wantchu.MyPage;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyPageListbuttons {

    public ArrayList<String> childText;
    public String parentName;

    public MyPageListbuttons(String parentName) {
        childText = new ArrayList<>();
        this.parentName = parentName;
    }
}
