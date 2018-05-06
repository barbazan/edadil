package com.barbazan.edadil.wicket.pages.good;

import com.barbazan.edadil.beans.GoodCategory;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;

import java.util.List;

public class CreateGoodCategoryPage extends BasePage {

    private GoodCategory goodCategory = new GoodCategory();

    public CreateGoodCategoryPage() {
        add(new Form<GoodCategory>("form", new CompoundPropertyModel<>(goodCategory)) {
            {
                add(new RequiredTextField<String>("name"));
            }
            @Override
            protected void onSubmit() {
                GoodCategory category = ShopDao.getGoodCategory(goodCategory.getName());
                if(category == null) {
                    HibernateContext.persist(goodCategory);
                    HibernateContext.commitSession();
                    HibernateContext.flushSession();
                    setResponsePage(CreateGoodCategoryPage.class);
                } else {
                    error("Категория товара с таким названием уже есть");
                }
            }
        });
        add(new ListView<GoodCategory>("goodCategories", new LoadableDetachableModel<List<GoodCategory>>() {
            @Override
            protected List<GoodCategory> load() {
                return ShopDao.getAllGoodCategories();
            }
        }) {
            @Override
            protected void populateItem(ListItem<GoodCategory> item) {
                item.add(new Label("name", (IModel<String>) () -> item.getModelObject().getName()));
            }
        });
    }
}
