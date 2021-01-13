package com.tpn.baro.JsonParsingHelper;

public class TypeListParsing {
    String typeName;
    String typeCode;
    String typeImage;

    public TypeListParsing(String typeName, String typeCode, String typeImage) {
        this.typeName = typeName;
        this.typeCode = typeCode;
        this.typeImage = typeImage;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public String getTypeImage() {
        return typeImage;
    }
}
