package com.barbazan.edadil.beans;

import java.io.Serializable;

public class Shop implements Serializable {

    private int id;
    private String name;
    private ShopCategory shopCategory;

    public Shop() {
    }

    public Shop(String name, ShopCategory shopCategory) {
        this.name = name;
        this.shopCategory = shopCategory;
    }

    public Shop(int id, String name, ShopCategory shopCategory) {
        this.id = id;
        this.name = name;
        this.shopCategory = shopCategory;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopCategory getShopCategory() {
        return shopCategory;
    }

    public void setShopCategory(ShopCategory shopCategory) {
        this.shopCategory = shopCategory;
    }
}
