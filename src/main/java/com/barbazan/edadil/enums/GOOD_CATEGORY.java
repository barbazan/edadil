package com.barbazan.edadil.enums;

public enum GOOD_CATEGORY {

    GOOD_CATEGORY_1("Продукты"),
    GOOD_CATEGORY_2("Алкоголь"),
    GOOD_CATEGORY_3("Для дома"),
    GOOD_CATEGORY_4("Косметика и гигиена"),
    GOOD_CATEGORY_5("Для детей"),
    GOOD_CATEGORY_6("Подарки"),
    GOOD_CATEGORY_7("Зоотовары"),
    GOOD_CATEGORY_8("Одежда и обувь"),
    GOOD_CATEGORY_9("Спорт и отдых"),
    GOOD_CATEGORY_10("Канцтовары"),
    GOOD_CATEGORY_11("Электроника"),
    GOOD_CATEGORY_12("Фастфуд");

    public String name;

    GOOD_CATEGORY(String name) {
        this.name = name;
    }
}
