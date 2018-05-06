package com.barbazan.edadil.wicket.pages;

import com.barbazan.edadil.wicket.pages.good.CreateGoodActionPage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodCategoryPage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodPage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodPricePage;
import com.barbazan.edadil.wicket.pages.shop.CreateShopCategoryPage;
import com.barbazan.edadil.wicket.pages.shop.CreateShopAddressPage;
import com.barbazan.edadil.wicket.pages.shop.CreateShopPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Page;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.request.mapper.parameter.PageParameters;

public class BasePage extends WebPage {

    public BasePage() {
        init();
    }

    public BasePage(PageParameters parameters) {
        super(parameters);
        init();
    }

    private void init() {
        add(new FeedbackPanel("feedbackPanel"));
        initMenu();
    }

    private void initMenu() {
        add(new BookmarkablePageLink<>("createShopCategoryLink", CreateShopCategoryPage.class).add(getMenuAttributeModifier(CreateShopCategoryPage.class)));
        add(new BookmarkablePageLink<>("createShopLink", CreateShopPage.class).add(getMenuAttributeModifier(CreateShopPage.class)));
        add(new BookmarkablePageLink<>("createShopAddressLink", CreateShopAddressPage.class).add(getMenuAttributeModifier(CreateShopAddressPage.class)));
        add(new BookmarkablePageLink<>("createGoodCategoryLink", CreateGoodCategoryPage.class).add(getMenuAttributeModifier(CreateGoodCategoryPage.class)));
        add(new BookmarkablePageLink<>("createGoodLink", CreateGoodPage.class).add(getMenuAttributeModifier(CreateGoodPage.class)));
        add(new BookmarkablePageLink<>("createGoodPriceLink", CreateGoodPricePage.class).add(getMenuAttributeModifier(CreateGoodPricePage.class)));
        add(new BookmarkablePageLink<>("createGoodActionLink", CreateGoodActionPage.class).add(getMenuAttributeModifier(CreateGoodActionPage.class)));
    }

    private AttributeModifier getMenuAttributeModifier(Class<? extends Page> pageClass) {
        return new AttributeModifier("class", pageClass == getPage().getClass() ? "selected" : "");
    }
}
