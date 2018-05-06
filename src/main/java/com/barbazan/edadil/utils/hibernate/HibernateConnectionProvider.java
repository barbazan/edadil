package com.barbazan.edadil.utils.hibernate;

import org.apache.commons.lang.math.NumberUtils;
import org.hibernate.HibernateException;
import org.hibernate.connection.ConnectionProvider;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Dmitry Malyshev
 * Date: 14.04.14 Time: 15:10
 * Email: dmitry.malyshev@gmail.com
 */
public class HibernateConnectionProvider implements ConnectionProvider {

    private ConnectionPool pool;

    public void configure(Properties properties) throws HibernateException {
        try {
            Driver driver = (Driver) Class.forName(properties.getProperty("connection.driver_class")).newInstance();
            int maxConnections = NumberUtils.toInt(properties.getProperty("connection.pool.maxConnections"), 10);
            pool = new ConnectionPool(driver
                    , properties.getProperty("connection.url")
                    , properties.getProperty("connection.username")
                    , properties.getProperty("connection.password")
                    , maxConnections
            );
        } catch (Throwable t) {
            throw new HibernateException("Init failed with errors:", t);
        }
    }

    public Connection getConnection() throws SQLException {
        if(pool != null) {
            return pool.getConnection();
        }
        return null;
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }

    public void close() throws HibernateException {
        pool.close();
    }

    public boolean supportsAggressiveRelease() {
        return false;
    }
}
