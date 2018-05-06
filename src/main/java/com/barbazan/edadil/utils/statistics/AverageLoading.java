package com.barbazan.edadil.utils.statistics;

import com.barbazan.edadil.utils.Time;

import java.util.ArrayList;
import java.util.Random;

/**
 * Dmitry Malyshev
 * Date: 12.10.14 Time: 15:10
 * Email: dmitry.malyshev@gmail.com
 */
public class AverageLoading {

    private long timeInterval;
    private int maxIntervals;
    private ArrayList<AverageCounter> list = new ArrayList<AverageCounter>();
    private AverageCounter currentCounter = new AverageCounter();
    private long lastShiftTime = System.currentTimeMillis();

    public AverageLoading() {
        this(Time.MINUTE_MILLIS, 60);
    }

    protected AverageLoading(long timeInterval, int maxIntervals) {
        this.timeInterval = timeInterval;
        this.maxIntervals = maxIntervals;
    }

    public void add(double value) {
        currentCounter.add(value);
        checkShift();
    }

    public double getAverage(int intervalsCount) {
        int count = 0;
        double average = 0;
        for(int i=0;i<list.size() && i<intervalsCount;i++) {
            count++;
            average += list.get(i).getAverage();
        }
        //add current
        average += currentCounter.getAverage();
        count++;
        return average / count;
    }

    public int getCount(int intervalsCount) {
        int count = 0;
        for(int i=0;i<list.size() && i<intervalsCount;i++) {
            count += list.get(i).getCount();
        }
        count += currentCounter.getCount();
        return count;
    }

    public int getCount() {
        return getCount(maxIntervals);
    }

    public double getAverage() {
        return getAverage(maxIntervals);
    }

    public double getLoading() {
        return getCount() * getAverage();
    }

    public String getLoadingAsString() {
        long k = 1000;
        long m = 1000 * k;
        long g = 1000 * m;
        long t = 1000 * g;
        double loading = getLoading();
        if(loading > t) {
            return Math.round(loading / t) + "t";
        } else if(loading > g) {
            return Math.round(loading / g) + "g";
        } else if(loading > m) {
            return Math.round(loading / m) + "m";
        } else if(loading > k) {
            return Math.round(loading / k) + "k";
        } else {
            return String.valueOf(Math.round(loading));
        }
    }

    private void checkShift() {
        if(System.currentTimeMillis() - lastShiftTime > timeInterval) {
            doShift();
        }
    }

    private synchronized void doShift() {
        if(System.currentTimeMillis() - lastShiftTime > timeInterval) {
            AverageCounter prevCounter = currentCounter;
            currentCounter = new AverageCounter();
            lastShiftTime = System.currentTimeMillis();
            while(list.size() >= maxIntervals) {
                list.remove(list.size() - 1);
            }
            list.add(0, prevCounter);
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Throwable {
        long startTime = System.currentTimeMillis();
        Random r = new Random();
        AverageLoading loading = new AverageLoading(10000, 10);
        while(true) {
            int v = r.nextInt(100);
            loading.add(v);
            Thread.sleep(10);
            long time = (System.currentTimeMillis() - startTime) / 1000;
            System.err.println("v=" + v + " av1=" + loading.getAverage(1) + " av5=" + loading.getAverage(5) + " c1=" + loading.getCount(1) + " c5=" + loading.getCount(5) + " time=" + time);
        }
    }
}
