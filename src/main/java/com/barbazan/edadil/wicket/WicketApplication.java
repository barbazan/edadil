package com.barbazan.edadil.wicket;

import com.barbazan.edadil.dao.ShopDao;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.barbazan.edadil.wicket.pages.HomePage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodActionPage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodCategoryPage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodPricePage;
import com.barbazan.edadil.wicket.pages.good.CreateGoodPage;
import com.barbazan.edadil.wicket.pages.shop.*;
import org.apache.log4j.Logger;
import org.apache.wicket.Application;
import org.apache.wicket.RuntimeConfigurationType;
import org.apache.wicket.Session;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.settings.RequestCycleSettings;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 */
public class WicketApplication extends WebApplication {

	public static final AtomicInteger connectionsCount = new AtomicInteger(0);

    private static Logger logger = Logger.getLogger(WicketApplication.class);

	@Override
	public Class<? extends WebPage> getHomePage() {
		return HomePage.class;
	}

	@Override
	public void init() {
        super.init();
        logger.info("*********************************************");
        HibernateContext.init();
        HibernateContext.getSession().getSessionFactory().getStatistics().setStatisticsEnabled(true);
        logger.info("Initialized success.");
        logger.info(".............................................");
        getPageSettings().setVersionPagesByDefault(false);
        getMarkupSettings().setCompressWhitespace(true);
        getRequestCycleSettings().setRenderStrategy(RequestCycleSettings.RenderStrategy.ONE_PASS_RENDER);
        getMarkupSettings().setDefaultMarkupEncoding("UTF-8");
        getRequestCycleSettings().setResponseRequestEncoding("UTF-8");

        mountPage("/home", HomePage.class);
        mountPage("/createShopCategory", CreateShopCategoryPage.class);
        mountPage("/createShop", CreateShopPage.class);
        mountPage("/createShopAddress", CreateShopAddressPage.class);
        mountPage("/createGoodCategory", CreateGoodCategoryPage.class);
        mountPage("/createGood", CreateGoodPage.class);
        mountPage("/createGoodPrice", CreateGoodPricePage.class);
        mountPage("/createGoodAction", CreateGoodActionPage.class);

        initDatabase();
    }

    @Override
    public Session newSession(Request request, Response response) {
        return new WicketSession(request);
    }

    public static boolean isDevMode() {
        return Application.get().getConfigurationType() == RuntimeConfigurationType.DEVELOPMENT;
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            logger.info("Destroy started...");
            logger.info("Commiting...");
            HibernateContext.closeSession(true);
            HibernateContext.destroy();
            logger.info("Destroy finished success. >>>>>>>>>");
        } catch (Throwable t) {
            logger.info("Destroy failed with errors:", t);
            logger.info("!!!!!!!!!!! Destroy failed. !!!!!!!!!!!");
        }
    }

    private void initDatabase() {
        ShopDao.initShopCategories();
        ShopDao.initShopTypes();
        ShopDao.initGoodCategories();
    }
}
