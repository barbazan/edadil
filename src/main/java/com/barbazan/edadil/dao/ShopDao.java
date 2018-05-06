package com.barbazan.edadil.dao;

import com.barbazan.edadil.beans.*;
import com.barbazan.edadil.enums.GOOD_CATEGORY;
import com.barbazan.edadil.enums.SHOP_CATEGORY;
import com.barbazan.edadil.enums.SHOP;
import com.barbazan.edadil.utils.hibernate.HibernateContext;

import java.util.List;

public class ShopDao {

    public static void initShopCategories() {
        for(SHOP_CATEGORY shop_category : SHOP_CATEGORY.values()) {
            ShopCategory shopCategory = getShopCategory(shop_category.name);
            if(shopCategory == null) {
                HibernateContext.getSession().persist(new ShopCategory(shop_category.name));
            }
        }
        HibernateContext.commitSession();
        HibernateContext.flushSession();
    }

    public static void initShopTypes() {
        for(SHOP shop_type : SHOP.values()) {
            Shop shopType = getShop(shop_type.name);
            if(shopType == null) {
                ShopCategory shopCategory = getShopCategory(SHOP_CATEGORY.SHOP_CATEGORY_1.name);
                HibernateContext.getSession().persist(new Shop(shop_type.name, shopCategory));
            }
        }
        HibernateContext.commitSession();
        HibernateContext.flushSession();
    }

    public static void initGoodCategories() {
        for(GOOD_CATEGORY good_category : GOOD_CATEGORY.values()) {
            GoodCategory goodCategory = getGoodCategory(good_category.name);
            if(goodCategory == null) {
                HibernateContext.getSession().persist(new GoodCategory(good_category.name));
            }
        }
        HibernateContext.commitSession();
        HibernateContext.flushSession();
    }

    public static Shop getShop(String name) {
        return (Shop)HibernateContext.getSession()
                .createQuery("from Shop where name = :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    public static ShopCategory getShopCategory(String name) {
        return (ShopCategory)HibernateContext.getSession()
                .createQuery("from ShopCategory where name = :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public static List<ShopCategory> getAllShopCategories() {
        return (List<ShopCategory>)HibernateContext.getSession()
                .createQuery("from ShopCategory")
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<Shop> getAllShops() {
        return (List<Shop>)HibernateContext.getSession()
                .createQuery("from Shop")
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<ShopAddress> getAllShopAddresses() {
        return (List<ShopAddress>)HibernateContext.getSession()
                .createQuery("from ShopAddress")
                .list();
    }

    public static ShopAddress getShopAddress(ShopAddress shopAddress) {
        return (ShopAddress)HibernateContext.getSession()
                .createQuery("from ShopAddress where shop.id = :shopId and street = :street and house = :house")
                .setInteger("shopId", shopAddress.getShop().getId())
                .setString("street", shopAddress.getStreet())
                .setString("house", shopAddress.getHouse())
                .setMaxResults(1)
                .uniqueResult();
    }

    public static GoodCategory getGoodCategory(String name) {
        return (GoodCategory)HibernateContext.getSession()
                .createQuery("from GoodCategory where name = :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public static List<GoodCategory> getAllGoodCategories() {
        return (List<GoodCategory>)HibernateContext.getSession()
                .createQuery("from GoodCategory")
                .list();
    }

    public static Good getGood(String name) {
        return (Good)HibernateContext.getSession()
                .createQuery("from Good where name = :name")
                .setString("name", name)
                .setMaxResults(1)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public static List<Good> getAllGoods() {
        return (List<Good>)HibernateContext.getSession()
                .createQuery("from Good")
                .list();
    }

    @SuppressWarnings("unchecked")
    public static GoodPrice getGoodPrice(GoodPrice goodPrice) {
        return (GoodPrice)HibernateContext.getSession()
                .createQuery("from GoodPrice where shop.id = :shopId and good.id = :goodId")
                .setInteger("shopId", goodPrice.getShop().getId())
                .setInteger("goodId", goodPrice.getGood().getId())
                .setMaxResults(1)
                .uniqueResult();
    }

    @SuppressWarnings("unchecked")
    public static List<GoodPrice> getGoodPricesInShop(int shopId) {
        return (List<GoodPrice>)HibernateContext.getSession()
                .createQuery("from GoodPrice where shop.id = :shopId order by good.id desc")
                .setInteger("shopId", shopId)
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<GoodPrice> getAllGoodPrices() {
        return (List<GoodPrice>)HibernateContext.getSession()
                .createQuery("from GoodPrice order by shop.id desc, good.id desc")
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<GoodAction> getGoodActionsInShop(int shopId) {
        return (List<GoodAction>)HibernateContext.getSession()
                .createQuery("from GoodAction where goodPrice.shop.id = :shopId")
                .setInteger("shopId", shopId)
                .list();
    }

    @SuppressWarnings("unchecked")
    public static List<GoodAction> getAllGoodActions() {
        return (List<GoodAction>)HibernateContext.getSession()
                .createQuery("from GoodAction order by goodPrice.shop.id desc, goodPrice.good.id desc")
                .list();
    }
}
