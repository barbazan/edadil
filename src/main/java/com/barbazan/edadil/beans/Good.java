package com.barbazan.edadil.beans;

import java.io.Serializable;

public class Good implements Serializable {

    private int id;
    private String name;
    private GoodCategory goodCategory;

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

    public GoodCategory getGoodCategory() {
        return goodCategory;
    }

    public void setGoodCategory(GoodCategory goodCategory) {
        this.goodCategory = goodCategory;
    }
}
