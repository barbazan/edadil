package com.barbazan.edadil.utils.statistics;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Dmitry Malyshev
 * Date: 12.10.14 Time: 15:10
 * Email: dmitry.malyshev@gmail.com
 */
public class Statistics {

    public static AverageLoading totalRequestTime = new AverageLoading();
    public static AverageLoading dbWaitTime = new AverageLoading();
    public static AverageLoading dbConnectingTime = new AverageLoading();
    public static AverageLoading htmlFetchTime = new AverageLoading();
    public static AverageLoading dbCommitTime = new AverageLoading();
    public static AverageLoading onlineReportingTime = new AverageLoading();

    private static Map<String, AverageLoading> pagesTimes = new ConcurrentHashMap<String, AverageLoading> ();

    public static void registerPageTime(String pageName, long time) {
        if(pageName != null) {
            AverageLoading al = pagesTimes.get(pageName);
            if(al == null) {
                al = new AverageLoading();
                pagesTimes.put(pageName, al);
            }
            al.add(time);
        }
    }

    public static List<String> getPagesNames() {
        return new ArrayList<String>(pagesTimes.keySet());
    }

    public static AverageLoading getAverageLoading(String key) {
        return pagesTimes.get(key);
    }
}
