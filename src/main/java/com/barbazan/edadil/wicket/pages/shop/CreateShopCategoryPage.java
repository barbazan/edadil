package com.barbazan.edadil.wicket.pages.shop;

import com.barbazan.edadil.beans.ShopCategory;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.*;

import java.util.List;

public class CreateShopCategoryPage extends BasePage {

    private ShopCategory shopCategory = new ShopCategory();

    public CreateShopCategoryPage() {
        add(new Form<ShopCategory>("form", new CompoundPropertyModel<>(shopCategory)) {
            {
                add(new RequiredTextField<String>("name"));
            }
            @Override
            protected void onSubmit() {
                String name = (String)get("name").getDefaultModelObject();
                if(name != null) {
                    ShopCategory sc = ShopDao.getShopCategory(name);
                    if(sc == null) {
                        HibernateContext.persist(shopCategory);
                        HibernateContext.commitSession();
                        HibernateContext.flushSession();
                        setResponsePage(CreateShopCategoryPage.class);
                    } else {
                        error("Категория магазин с таким названием уже есть");
                    }
                    super.onSubmit();
                }
            }
        });
        add(new ListView<ShopCategory>("shopCategories", new LoadableDetachableModel<List<ShopCategory>>() {
            @Override
            protected List<ShopCategory> load() {
                return ShopDao.getAllShopCategories();
            }
        }) {
            @Override
            protected void populateItem(ListItem<ShopCategory> item) {
                item.add(new Label("name", (IModel<String>) () -> item.getModelObject().getName()));
            }
        });
    }
}
