package com.barbazan.edadil.utils.http;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodCategory;
import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopCategory;
import com.barbazan.edadil.json.GoodCategoryConverter;
import com.barbazan.edadil.json.GoodConverter;
import com.barbazan.edadil.json.ShopCategoryConverter;
import com.barbazan.edadil.json.ShopConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class JsonUtil {

    public static String toJson(Object object) {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ShopCategory.class, new ShopCategoryConverter());
        builder.registerTypeAdapter(GoodCategory.class, new GoodCategoryConverter());
        builder.registerTypeAdapter(Shop.class, new ShopConverter());
        builder.registerTypeAdapter(Good.class, new GoodConverter());
        Gson gson = builder.create();
        return gson.toJson(object);
    }

//    public static fromJson(String json, Type type) {
//        GsonBuilder builder = new GsonBuilder();
//        builder.registerTypeAdapter(ShopCategory.class, new ShopCategoryConverter());
//        builder.registerTypeAdapter(GoodCategory.class, new GoodCategoryConverter());
//        builder.registerTypeAdapter(Shop.class, new ShopConverter());
//        builder.registerTypeAdapter(Good.class, new GoodConverter());
//        Gson gson = builder.create();
//        return gson.fromJson(json, type);
//    }
}
