package com.barbazan.edadil.wicket.pages.shop;

import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.beans.ShopAddress;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

public class CreateShopAddressPage extends BasePage {

    private ShopAddress shopAddress = new ShopAddress();

    public CreateShopAddressPage() {
        add(new Form<Shop>("form") {
            {
                add(new DropDownChoice<Shop>("shop", new PropertyModel<>(shopAddress, "shop"), new LoadableDetachableModel<List<? extends Shop>>() {
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
                add(new RequiredTextField<String>("street", new PropertyModel<>(shopAddress, "street")));
                add(new RequiredTextField<String>("house", new PropertyModel<>(shopAddress, "house")));
            }
            @Override
            protected void onSubmit() {
                ShopAddress address = ShopDao.getShopAddress(shopAddress);
                if(address == null) {
                    HibernateContext.persist(shopAddress);
                    HibernateContext.commitSession();
                    HibernateContext.flushSession();
                    setResponsePage(CreateShopAddressPage.class);
                } else {
                    error("Магазин с таким адресом уже есть");
                }
            }
        });
        add(new ListView<ShopAddress>("shopAddresses", new LoadableDetachableModel<List<ShopAddress>>() {
            @Override
            protected List<ShopAddress> load() {
                return ShopDao.getAllShopAddresses();
            }
        }) {
            @Override
            protected void populateItem(ListItem<ShopAddress> item) {
                item.add(new Label("shop", (IModel<String>) () -> item.getModelObject().getShop().getName()));
                item.add(new Label("address", (IModel<String>) () -> item.getModelObject().getStreet() + ", " + item.getModelObject().getHouse()));
            }
        });
    }
}
