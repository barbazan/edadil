package com.barbazan.edadil.wicket.pages.good;

import com.barbazan.edadil.beans.Good;
import com.barbazan.edadil.beans.GoodAction;
import com.barbazan.edadil.beans.Shop;
import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.Time;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.BasePage;
import com.googlecode.wicket.jquery.core.Options;
import com.googlecode.wicket.jquery.ui.form.datepicker.DatePicker;
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
        add(new Form<GoodAction>("form", new CompoundPropertyModel<>(goodAction)) {
            {
                add(new DropDownChoice<>("shop", new LoadableDetachableModel<List<? extends Shop>>() {
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
                add(new DropDownChoice<>("good", new LoadableDetachableModel<List<? extends Good>>() {
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
                add(new DatePicker("startDate", "dd/MM/yyyy", new Options()).setRequired(true));
                add(new DatePicker("endDate", "dd/MM/yyyy", new Options()).setRequired(true));
                add(new RequiredTextField<Float>("price"));
                add(new RequiredTextField<Float>("discountPrice"));
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
                if(goodAction.getShop() == null) {
                    return ShopDao.getAllGoodActions();
                } else {
                    return ShopDao.getGoodActionsInShop(goodAction.getShop().getId());
                }
            }
        }) {
            @Override
            protected void populateItem(ListItem<GoodAction> item) {
                item.add(new Label("shop", (IModel<String>) () -> item.getModelObject().getShop().getName()));
                item.add(new Label("good", (IModel<String>) () -> item.getModelObject().getGood().getName()));
                item.add(new Label("startDate", (IModel<String>) () -> Time.DATE_FORMAT_2.format(item.getModelObject().getStartDate())));
                item.add(new Label("endDate", (IModel<String>) () -> Time.DATE_FORMAT_2.format(item.getModelObject().getEndDate())));
                item.add(new Label("price", (IModel<String>) () -> String.valueOf(item.getModelObject().getPrice())));
                item.add(new Label("discountPrice", (IModel<String>) () -> String.valueOf(item.getModelObject().getDiscountPrice())));
                item.add(new Label("discount", (IModel<String>) () -> String.valueOf(item.getModelObject().getDiscount() + " %")));
            }
        });
    }
}
