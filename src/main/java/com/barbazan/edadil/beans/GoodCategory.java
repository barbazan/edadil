package com.barbazan.edadil.beans;

import java.io.Serializable;

public class GoodCategory implements Serializable {

    private int id;
    private String name;

    public GoodCategory() {
    }

    public GoodCategory(String name) {
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
