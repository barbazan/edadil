package com.barbazan.edadil.http;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author Yaroslav S Malyshev (yaroslav.malyshev@gmail.com) 25.06.2014
 */
public class StaticCacheFilter implements Filter {

    private static final int DEFAULT_HOURS = 24 * 30;

    private int hours;
    private String exclude;

    private static final Logger logger = getLogger(StaticCacheFilter.class);

    public void init(FilterConfig filterConfig) throws ServletException {
        this.hours = NumberUtils.toInt(filterConfig.getInitParameter("hours"), DEFAULT_HOURS);
        this.exclude = filterConfig.getInitParameter("exclude");
        logger.info("Initialized with time = " + hours + "h, exclude = " + exclude);
    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;
        boolean enabled = true;
        if (exclude != null) {
            String uri = StringUtils.defaultString(req.getRequestURI());
            enabled = !uri.matches(exclude);
        }
        if (enabled) {
            long timeInSeconds = hours * 3600;
            resp.setHeader("Cache-Control", "max-age=" + timeInSeconds);
            resp.setDateHeader("Expires", System.currentTimeMillis() + (timeInSeconds * 1000));
        }
        chain.doFilter(request, response);
    }
}

