package com.barbazan.edadil.utils.hibernate;

import com.barbazan.edadil.wicket.WicketApplication;
import com.barbazan.edadil.utils.statistics.Statistics;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;


/**
 * Dmitry Malyshev
 * Date: 12.10.14 Time: 15:10
 * Email: dmitry.malyshev@gmail.com
 */
public class ConnectionPool {

    private static Logger logger = Logger.getLogger(ConnectionPool.class);

    public static long TIME_TO_LIVE = 300000;

    private Driver driver;
    private String url;
    private Properties dbProperties;
    private int maxConnections;
    private int busyConnections;
    private boolean closed = false;

    private ConcurrentLinkedQueue<PooledConnection> connections = new ConcurrentLinkedQueue<PooledConnection>();

    public ConnectionPool(Driver driver, String url, String user, String password, int maxConnections) {
        this.driver = driver;
        this.url = url;
        this.dbProperties = new Properties();
        dbProperties.put("user", user == null ? "" : user);
        dbProperties.put("password", password == null ? "" : password);
        this.maxConnections = maxConnections;
        logger.info("Initialized url:" + url + " user:" + user + " password:" + password + " maxConnections:" + maxConnections);

        Thread thread = new Thread(new ConnectionsMonitor(), "Connections-Monitor");
        thread.setDaemon(true);
        thread.start();
    }

    public Connection getConnection() throws SQLException {
        if (closed) {
            throw new IllegalStateException("Pool is closed");
        }

        PooledConnection connection = waitAndGetFreeConnection();

        WicketApplication.connectionsCount.incrementAndGet();
        return connection;
    }

    public void close() {
        logger.info("Closing...");
        Set<PooledConnection> connectionsForClose = new HashSet<PooledConnection>();
        synchronized (this) {
            closed = true;
            connectionsForClose.addAll(connections);
            connections.clear();
        }
        logger.info("Found " + connectionsForClose.size() + " connections for closing.");
        for(PooledConnection connection:connectionsForClose) {
            realCloseConnection(connection);
        }
        logger.info("Closed success.");
    }

    void connectionClosed(PooledConnection pooledConnection) throws SQLException {
        if (pooledConnection == null) {
            throw new NullPointerException("Null connection");
        }
        try {
            if (pooledConnection.isExpired() || closed) {
                realCloseConnection(pooledConnection);
                synchronized (this) {
                    busyConnections--;
                }
            } else {
                synchronized (this) {
                    connections.add(pooledConnection);
                    busyConnections--;
                    this.notify();
                }
            }
        } finally {
            WicketApplication.connectionsCount.decrementAndGet();
        }
    }

    private String getPoolSize() {
        synchronized (this) {
            return "busy=" + busyConnections + ", free=" + connections.size();
        }
    }
    
    private PooledConnection waitAndGetFreeConnection() {

        long startTime = System.currentTimeMillis();
        PooledConnection connection;

        synchronized (this) {
            while (!closed && connections.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    //do nothing
                }
            }
            if(closed) {
                throw new RuntimeException("Sorry, pool closed.");
            }
            connection = connections.poll();
            busyConnections++;
        }

        Statistics.dbWaitTime.add(System.currentTimeMillis() - startTime);
        return connection;
    }

    private void realCloseConnection(PooledConnection pooledConnection) {
        try {
            pooledConnection.getConnection().close();
            logger.info("Closed connection " + getPoolSize());
        } catch (Throwable t) {
            logger.error("", t);
        }
    }

    private PooledConnection newConnection() throws SQLException {
        long connectTime = -System.currentTimeMillis();
        Connection realCon = driver.connect(url, dbProperties);
        realCon.setAutoCommit(false);
        logger.info("Opened connection " + getPoolSize());
        connectTime += System.currentTimeMillis();
        Statistics.dbConnectingTime.add(connectTime);
        return new PooledConnection(realCon, this);
    }

    private class ConnectionsMonitor implements Runnable {
        @Override
        public void run() {
            logger.info("ConnectionsMonitor started.");
            while (!closed) {

                int totalConnections;
                int freeConnections;
                synchronized (ConnectionPool.this) {
                    freeConnections = connections.size();
                    totalConnections = busyConnections + freeConnections;
                }

                boolean opened = false;
                if(freeConnections == 0 && totalConnections < maxConnections) {
                    try {
                        PooledConnection newConnection = newConnection();
                        synchronized (ConnectionPool.this) {
                            connections.add(newConnection);
                            ConnectionPool.this.notify();
                        }
                        opened = true;
                    } catch (Throwable t) {
                        logger.error("", t);
                    }
                }

                //Sleep if not need open
                if(!opened) {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        //do nothing
                    }
                }
            }
        }
    }
}
