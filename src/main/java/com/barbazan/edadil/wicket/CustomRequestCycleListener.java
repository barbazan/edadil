package com.barbazan.edadil.wicket;

import com.barbazan.edadil.utils.Time;
import com.barbazan.edadil.utils.hibernate.HibernateContext;
import com.google.common.base.Throwables;
import org.apache.commons.lang3.StringUtils;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.IRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.http.WebRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketException;

/**
 * Author: Yaroslav Rudykh (slavan.it2me@gmail.com) Date: 07.11.14
 */
public class CustomRequestCycleListener implements IRequestCycleListener {

    private static final Logger logger = LoggerFactory.getLogger(CustomRequestCycleListener.class);

    @Override
    public void onEndRequest(RequestCycle cycle) {
        HibernateContext.closeSession(true);

        long requestTime = System.currentTimeMillis() - cycle.getStartTime();
        if (requestTime > Time.SECOND_MILLIS * 10) {
            logger.warn("TOO LONG REQUEST: {}ms at {}", requestTime, getRequestUrl());
        }

        MDC.clear();
    }

    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        HibernateContext.closeSession(false);
        //noinspection ThrowableResultOfMethodCallIgnored
        Throwable rootCause = Throwables.getRootCause(ex);
        if (rootCause instanceof SocketException) {
            String requestUrl = getRequestUrl();
            processSocketException(rootCause, requestUrl);
        } else if(rootCause instanceof IOException && "Broken pipe".equals(rootCause.getMessage())) {
            error("Broken pipe exception ignored");
        } else {
            error("", ex);
        }
        return null;
    }

    private void processSocketException(Throwable rootCause, String requestUrl) {
        if (!StringUtils.equals(rootCause.getMessage(), "Broken pipe")) {
            return;
        }
        error("SocketException: " + rootCause.getMessage() + ", url is " + requestUrl);
    }

    private String getRequestUrl() {
        // this is a wicket-specific request interface
        final Request request = RequestCycle.get().getRequest();
        if (request instanceof WebRequest) {
            final WebRequest wr = (WebRequest) request;
            // but this is the real thing
            final HttpServletRequest hsr = (HttpServletRequest) wr.getContainerRequest();
            String reqUrl = hsr.getRequestURL().toString();
            final String queryString = hsr.getQueryString();
            if (queryString != null) {
                reqUrl += "?" + queryString;
            }
            return reqUrl;
        }
        return null;
    }

    protected void error(String msg, Throwable throwable) {
        logger.error(msg, throwable);
    }

    protected void error(String msg) {
        logger.error(msg);
    }

    protected void info(String msg) {
        logger.info(msg);
    }
}
