package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class TypeParsing {

    boolean result;
    String message;
    ArrayList<TypeListParsing> typeList = new ArrayList<TypeListParsing>();

    public void setResult(boolean result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTypeList(ArrayList<TypeListParsing> typeList) {
        this.typeList = typeList;
    }
    public boolean isResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public ArrayList<TypeListParsing> getTypeList(){ return typeList; }
}
