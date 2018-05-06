package com.barbazan.edadil.enums;

public enum SHOP_CATEGORY {

    SHOP_CATEGORY_1("Супермаркеты"),
    SHOP_CATEGORY_2("Детские магазины"),
    SHOP_CATEGORY_3("Косметика и быт"),
    SHOP_CATEGORY_4("Товары для дома"),
    SHOP_CATEGORY_5("Рестораны");

    public String name;

    SHOP_CATEGORY(String name) {
        this.name = name;
    }
}
