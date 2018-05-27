package com.barbazan.edadil.wicket.pages.json;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodCategory;
import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopCategory;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.json.GoodCategoryConverter;
import com.barbazan.edadil.json.GoodConverter;
import com.barbazan.edadil.json.ShopCategoryConverter;
import com.barbazan.edadil.json.ShopConverter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class GetJsonPage extends WebPage {

    private static final String METHOD_PARAM_NAME = "m";
    private static final String METHOD_GET_SHOP_CATEGORIES = "get_shop_categories";
    private static final String METHOD_GET_SHOPS = "get_shops";
    private static final String METHOD_GET_SHOP_ADDRESSES = "get_shop_addresses";
    private static final String METHOD_GET_GOOD_CATEGORIES = "get_good_categories";
    private static final String METHOD_GET_GOODS = "get_goods";
    private static final String METHOD_GET_GOOD_ACTIONS = "get_good_actions";

    public GetJsonPage(PageParameters parameters) {
        super(parameters);
        String method = parameters.get(METHOD_PARAM_NAME).toString("");
        add(new Label("data", (IModel<String>) () -> {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(ShopCategory.class, new ShopCategoryConverter());
            builder.registerTypeAdapter(GoodCategory.class, new GoodCategoryConverter());
            builder.registerTypeAdapter(Shop.class, new ShopConverter());
            builder.registerTypeAdapter(Good.class, new GoodConverter());
            Gson gson = builder.create();
            switch (method) {
                case METHOD_GET_SHOP_CATEGORIES: return gson.toJson(ShopDao.getAllShopCategories());
                case METHOD_GET_SHOPS: return gson.toJson(ShopDao.getAllShops());
                case METHOD_GET_SHOP_ADDRESSES: return gson.toJson(ShopDao.getAllShopAddresses());
                case METHOD_GET_GOOD_CATEGORIES: return gson.toJson(ShopDao.getAllGoodCategories());
                case METHOD_GET_GOODS: return gson.toJson(ShopDao.getAllGoods());
                case METHOD_GET_GOOD_ACTIONS: return gson.toJson(ShopDao.getAllGoodActions());
            }
            return "";
        }).setRenderBodyOnly(true));
    }
}
