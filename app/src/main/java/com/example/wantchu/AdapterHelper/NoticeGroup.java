package com.example.wantchu.AdapterHelper;

import java.util.ArrayList;
public class NoticeGroup {
    public ArrayList<String> childContent;
    public ArrayList<String> childTitle;
    public ArrayList<String> childDate;
    public String groupName;
    public NoticeGroup(String name) {
        groupName = name;
        childContent = new ArrayList<String>();
        childTitle = new ArrayList<String>();
        childDate = new ArrayList<String>();
    }
}