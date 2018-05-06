package com.barbazan.edadil.wicket.pages.good;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodCategory;
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
import org.apache.wicket.model.*;

import java.util.List;

public class CreateGoodPage extends BasePage {

    private Good good = new Good();

    public CreateGoodPage() {
        add(new Form<Good>("form", new CompoundPropertyModel<>(good)) {
            {
                add(new DropDownChoice<>("goodCategory", new LoadableDetachableModel<List<? extends GoodCategory>>() {
                    @Override
                    protected List<? extends GoodCategory> load() {
                        return ShopDao.getAllGoodCategories();
                    }
                }, new IChoiceRenderer<GoodCategory>() {
                    @Override
                    public Object getDisplayValue(GoodCategory object) {
                        return object.getName();
                    }

                    @Override
                    public String getIdValue(GoodCategory object, int index) {
                        return String.valueOf(object.getId());
                    }

                    @Override
                    public GoodCategory getObject(String id, IModel<? extends List<? extends GoodCategory>> choices) {
                        return HibernateContext.get(GoodCategory.class, Integer.parseInt(id));
                    }
                }).setRequired(true));

                add(new RequiredTextField<String>("name"));
            }
            @Override
            protected void onSubmit() {
                Good g = ShopDao.getGood(good.getName());
                if(g == null) {
                    HibernateContext.persist(good);
                    HibernateContext.commitSession();
                    HibernateContext.flushSession();
                    setResponsePage(CreateGoodPage.class);
                } else {
                    error("Товар с таким названием уже есть");
                }
            }
        });
        add(new ListView<Good>("goods", new LoadableDetachableModel<List<Good>>() {
            @Override
            protected List<Good> load() {
                return ShopDao.getAllGoods();
            }
        }) {
            @Override
            protected void populateItem(ListItem<Good> item) {
                item.add(new Label("goodName", (IModel<String>) () -> item.getModelObject().getName()));
                item.add(new Label("goodCategory", (IModel<String>) () -> item.getModelObject().getGoodCategory().getName()));
            }
        });
    }
}
