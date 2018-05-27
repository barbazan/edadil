package com.barbazan.edadil.wicket.pages.json;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodCategory;
import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopCategory;
import com.barbazan.edadil.json.GoodCategoryConverter;
import com.barbazan.edadil.json.GoodConverter;
import com.barbazan.edadil.json.ShopCategoryConverter;
import com.barbazan.edadil.json.ShopConverter;
import com.barbazan.edadil.utils.http.HttpUtil;
import com.barbazan.edadil.wicket.pages.BasePage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

public class TestJsonPage extends BasePage {

    public TestJsonPage() {
        try {
            String content = HttpUtil.getContent("http://edadil.dungeo.mobi/getJson?m=get_good_categories");
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ShopCategory.class, new ShopCategoryConverter());
            builder.registerTypeAdapter(GoodCategory.class, new GoodCategoryConverter());
            builder.registerTypeAdapter(Shop.class, new ShopConverter());
            builder.registerTypeAdapter(Good.class, new GoodConverter());
            Gson gson = builder.create();
            GoodCategory[] goodCategories = gson.fromJson(content, GoodCategory[].class);
            for (GoodCategory goodCategory : goodCategories) {
                System.out.println("goodCategory = " + goodCategory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
