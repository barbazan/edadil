package com.barbazan.edadil.wicket.pages.shop;

import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopCategory;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodActionPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class CreateShopPage extends BasePage {

    private Shop shop = new Shop();

    public CreateShopPage() {
        add(new Form<Shop>("form", new CompoundPropertyModel<>(shop)) {
            {
                add(new RequiredTextField<String>("name"));
                add(new DropDownChoice<>("shopCategory", new LoadableDetachableModel<List<? extends ShopCategory>>() {
                    @Override
                    protected List<? extends ShopCategory> load() {
                        return ShopDao.getAllShopCategories();
                    }
                }, new IChoiceRenderer<ShopCategory>() {
                    @Override
                    public Object getDisplayValue(ShopCategory object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(ShopCategory object, int index) {
                        return String.valueOf(object.getId());
                    }

                    @Override
                    public ShopCategory getObject(String id, IModel<? extends List<? extends ShopCategory>> choices) {
                        return HibernateContext.get(ShopCategory.class, Integer.parseInt(id));
                    }
                }).setRequired(true));
            }
            @Override
            protected void onSubmit() {
                Shop s = ShopDao.getShop(shop.getName());
                if(s == null) {
                    HibernateContext.persist(shop);
                    HibernateContext.commitSession();
                    HibernateContext.flushSession();
                    setResponsePage(CreateShopPage.class);
                } else {
                    error("Магазин с таким названием уже есть");
                }
            }
        });
        add(new ListView<Shop>("shops", new LoadableDetachableModel<List<Shop>>() {
            @Override
            protected List<Shop> load() {
                return ShopDao.getAllShops();
            }
        }) {
            @Override
            protected void populateItem(ListItem<Shop> item) {
                Link link = new BookmarkablePageLink("shopLink", CreateGoodActionPage.class);
                link.add(new Label("name", (IModel<String>) () -> item.getModelObject().getName()));
                item.add(link);
                item.add(new Label("shopCategory", (IModel<String>) () -> item.getModelObject().getShopCategory().getName()));
            }
        });
    }
}
