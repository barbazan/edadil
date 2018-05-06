package com.barbazan.edadil.wicket.pages.good;

import com.barbazan.edadil.beans.GoodAction;
import com.barbazan.edadil.beans.GoodPrice;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import java.util.List;

public class CreateGoodActionPage extends BasePage {

    private GoodAction goodAction = new GoodAction();

    public CreateGoodActionPage(PageParameters parameters) {
        super(parameters);
        int shopId = parameters.get("shopId").toInt(0);
        add(new Form<GoodAction>("form", new CompoundPropertyModel<>(goodAction)) {
            {
                add(new DropDownChoice<>("goodPrice", new LoadableDetachableModel<List<? extends GoodPrice>>() {
                    @Override
                    protected List<? extends GoodPrice> load() {
                        if(shopId == 0) {
                            return ShopDao.getAllGoodPrices();
                        } else {
                            return ShopDao.getGoodPricesInShop(shopId);
                        }

                    }
                }, new IChoiceRenderer<GoodPrice>() {
                    @Override
                    public Object getDisplayValue(GoodPrice object) {
                        return object.getShop().getName() + ", " + object.getGood().getName();
                    }

                    @Override
                    public String getIdValue(GoodPrice object, int index) {
                        return String.valueOf(object.getId());
                    }

                    @Override
                    public GoodPrice getObject(String id, IModel<? extends List<? extends GoodPrice>> choices) {
                        for(GoodPrice goodPrice : choices.getObject()) {
                            if(goodPrice.getId() == Integer.parseInt(id)) {
                                return goodPrice;
                            }
                        }
                        return null;
                    }
                }).setRequired(true));
                add(new DateTextField("startDate", "dd/MM/yyyy"));
                add(new DateTextField("endDate", "dd/MM/yyyy"));
                add(new RequiredTextField<Float>("discount"));
            }
            @Override
            protected void onSubmit() {
                HibernateContext.persist(goodAction);
                HibernateContext.commitSession();
                HibernateContext.flushSession();
                setResponsePage(CreateGoodActionPage.class, getPageParameters());
            }
        });
        add(new ListView<GoodAction>("goodActions", new LoadableDetachableModel<List<GoodAction>>() {
            @Override
            protected List<GoodAction> load() {
                if(shopId == 0) {
                    return ShopDao.getAllGoodActions();
                } else {
                    return ShopDao.getGoodActionsInShop(shopId);
                }
            }
        }) {
            @Override
            protected void populateItem(ListItem<GoodAction> item) {
                item.add(new Label("shop", (IModel<String>) () -> item.getModelObject().getGoodPrice().getShop().getName()));
                item.add(new Label("good", (IModel<String>) () -> item.getModelObject().getGoodPrice().getGood().getName()));
                item.add(new Label("startDate", (IModel<String>) () -> String.valueOf(item.getModelObject().getStartDate())));
                item.add(new Label("endDate", (IModel<String>) () -> String.valueOf(item.getModelObject().getEndDate())));
                item.add(new Label("price", (IModel<String>) () -> String.valueOf(item.getModelObject().getGoodPrice().getPrice())));
                item.add(new Label("discount", (IModel<String>) () -> String.valueOf(Math.round(item.getModelObject().getDiscount() * 100)) + " %"));
                item.add(new Label("discountPrice", (IModel<String>) () -> String.valueOf(item.getModelObject().getGoodPrice().getPrice() - item.getModelObject().getGoodPrice().getPrice() * item.getModelObject().getDiscount())));
            }
        });
    }
}
