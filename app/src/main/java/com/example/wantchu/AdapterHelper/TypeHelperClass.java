package com.example.wantchu.AdapterHelper;


import java.util.ArrayList;


public class TypeHelperClass {
    public ArrayList<String> typeNames;
    public ArrayList<String> typeCodes;
    public ArrayList<String> typeImages;
    public String typeName;
    public String typeCode;
    public String typeImage;

    public TypeHelperClass(String typeName, String typeCode, String typeImage) {
        typeNames = new ArrayList<>();
        typeCodes = new ArrayList<>();
        typeImages = new ArrayList<>();
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.typeImage = typeImage;
    }

}
