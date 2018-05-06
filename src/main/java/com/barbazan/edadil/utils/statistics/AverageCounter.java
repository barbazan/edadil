package com.barbazan.edadil.utils.statistics;

import java.util.Random;

/**
 * Dmitry Malyshev
 * Date: 12.10.14 Time: 15:10
 * Email: dmitry.malyshev@gmail.com
 */
public class AverageCounter {

    private double average;
    private int count;

    public synchronized void add(double value) {
        count ++;
        double delta = (value - average) / count;
        average += delta;
    }

    public double getAverage() {
        return average;
    }

    public int getCount() {
        return count;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Throwable {
        AverageCounter counter = new AverageCounter();
        Random r = new Random();
        while(true) {
            counter.add(r.nextInt(100));
            System.err.println("av" + counter.getAverage() + " c=" + counter.getCount());
            Thread.sleep(10);
        }
    }
}
