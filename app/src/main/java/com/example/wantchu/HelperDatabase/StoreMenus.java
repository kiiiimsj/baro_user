package com.example.wantchu.HelperDatabase;

public class StoreMenus {
    private int storeId;
    private int categoryId;
    private String menuInfo;
    private String menuName;
    private int menuDefaultprice;
    private int menuId;
    private String menuImage;

    public StoreMenus() { }
    public StoreMenus(int storeId, int categoryId, String menuInfo, String menuName, int menuDefaultprice, int menuId, String menuImage) {
        this.storeId = storeId;
        this.categoryId = categoryId;
        this.menuInfo = menuInfo;
        this.menuName = menuName;
        this.menuDefaultprice = menuDefaultprice;
        this.menuId = menuId;
        this.menuImage = menuImage;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(String menuInfo) {
        this.menuInfo = menuInfo;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuDefaultprice() {
        return menuDefaultprice;
    }

    public void setMenuDefaultprice(int menuDefaultprice) {
        this.menuDefaultprice = menuDefaultprice;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getMenuImage(){return menuImage;}
    public void setMenuImage(String menuImage){
        this.menuImage = menuImage;
    }

    @Override
    public String toString() {
        return "StoreMenus{" +
                "storeId=" + storeId +
                ", categoryId=" + categoryId +
                ", menuInfo='" + menuInfo + '\'' +
                ", menuName='" + menuName + '\'' +
                ", menuDefaultprice=" + menuDefaultprice +
                ", menuId=" + menuId +
                ", menuImage='" + menuImage +
                '}';
    }
}
