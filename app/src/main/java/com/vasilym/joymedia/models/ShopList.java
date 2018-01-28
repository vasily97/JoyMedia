package com.vasilym.joymedia.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopList {

    @SerializedName("shop")
    @Expose
    private List<Shop> shop = null;

    public List<Shop> getShop() {
        return shop;
    }

    public void setShop(List<Shop> shop) {
        this.shop = shop;
    }

}