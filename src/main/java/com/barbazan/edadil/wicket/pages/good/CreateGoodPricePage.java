package com.barbazan.edadil.wicket.pages.good;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodPrice;
import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class CreateGoodPricePage extends BasePage {

    private GoodPrice goodPrice = new GoodPrice();

    public CreateGoodPricePage(PageParameters parameters) {
        int shopId = parameters.get("shopId").toInt(0);
        add(new Form<Good>("form") {
            {
                add(new DropDownChoice<>("shop", new PropertyModel<>(goodPrice, "shop"), new LoadableDetachableModel<List<? extends Shop>>() {
                    @Override
                    protected List<? extends Shop> load() {
                        return ShopDao.getAllShops();
                    }
                }, new IChoiceRenderer<Shop>() {
                    @Override
                    public Object getDisplayValue(Shop object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(Shop object, int index) {
                        return String.valueOf(object.getId());
                    }

                    @Override
                    public Shop getObject(String id, IModel<? extends List<? extends Shop>> choices) {
                        return HibernateContext.get(Shop.class, Integer.parseInt(id));
                    }
                }).setRequired(true));
                add(new DropDownChoice<>("good", new PropertyModel<>(goodPrice, "good"), new LoadableDetachableModel<List<? extends Good>>() {
                    @Override
                    protected List<? extends Good> load() {
                        return ShopDao.getAllGoods();
                    }
                }, new IChoiceRenderer<Good>() {
                    @Override
                    public Object getDisplayValue(Good object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(Good object, int index) {
                        return String.valueOf(object.getId());
                    }

                    @Override
                    public Good getObject(String id, IModel<? extends List<? extends Good>> choices) {
                        return HibernateContext.get(Good.class, Integer.parseInt(id));
                    }
                }).setRequired(true));
                add(new RequiredTextField<Float>("price", new PropertyModel<>(goodPrice,"price")));
            }
            @Override
            protected void onSubmit() {
                GoodPrice gp = ShopDao.getGoodPrice(goodPrice);
                if(gp == null) {
                    HibernateContext.persist(goodPrice);
                    HibernateContext.commitSession();
                    HibernateContext.flushSession();
                    setResponsePage(CreateGoodPricePage.class, new PageParameters().set("shopId", goodPrice.getShop().getId()));
                } else {
                    error("Цена для этого товара в данном магазине уже есть");
                }
            }
        });
        add(new ListView<GoodPrice>("goodPrices", new LoadableDetachableModel<List<GoodPrice>>() {
            @Override
            protected List<GoodPrice> load() {
                if(shopId == 0) {
                    return ShopDao.getAllGoodPrices();
                } else {
                    return ShopDao.getGoodPricesInShop(shopId);
                }
            }
        }) {
            @Override
            protected void populateItem(ListItem<GoodPrice> item) {
                item.add(new Label("shop", (IModel<String>) () -> item.getModelObject().getShop().getName()));
                BookmarkablePageLink link = new BookmarkablePageLink<>("goodLink", CreateGoodActionPage.class, new PageParameters().set("shopId", item.getModelObject().getShop().getId()));
                link.add(new Label("good", (IModel<String>) () -> item.getModelObject().getGood().getName()));
                item.add(link);
                item.add(new Label("price", (IModel<String>) () -> String.valueOf(item.getModelObject().getPrice())));
            }
        });
    }
}
