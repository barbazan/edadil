package com.barbazan.edadil.beans;

import com.barbazan.edadil.enums.SHOP_CATEGORY;

import java.io.Serializable;

public class ShopCategory implements Serializable {

    private int id;
    private String name;

    public ShopCategory() {
    }

    public ShopCategory(SHOP_CATEGORY shop_category) {
        this(shop_category.name);
    }

    public ShopCategory(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public ShopCategory(String name) {
        this.name = name;
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
}
