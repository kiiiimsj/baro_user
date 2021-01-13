package com.example.wantchu.JsonParsingHelper;

import java.util.ArrayList;

public class FavoriteParsing {
    boolean result;
    String message;

    ArrayList<FavoriteListParsing> favorite = new ArrayList<>();

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<FavoriteListParsing> getFavorite() {
        return favorite;
    }

    public void setFavorite(ArrayList<FavoriteListParsing> favorite) {
        this.favorite = favorite;
    }
}
