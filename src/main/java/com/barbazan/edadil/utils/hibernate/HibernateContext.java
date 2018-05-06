package com.barbazan.edadil.utils.hibernate;

import com.barbazan.edadil.utils.Time;
import com.barbazan.edadil.utils.statistics.Statistics;
import org.apache.log4j.Logger;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Dmitry Malyshev
 * Date: 26.11.13 Time: 16:26
 * Email: dmitry.malyshev@gmail.com
 */
public class HibernateContext {

    private static final Logger logger = Logger.getLogger(HibernateContext.class);

    private static SessionFactory sessionFactory;
    private static ThreadLocal<Session> session = new ThreadLocal<Session>();

    public static void init() {
        try {
            logger.info("Initializing...");
            Configuration configuration = new Configuration();
            configuration.configure();
//            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder()
//                    .applySettings(configuration.getProperties())
//                    .buildServiceRegistry();
            sessionFactory = configuration.buildSessionFactory();
            logger.info("Initialized success.");
        } catch (Throwable t) {
            logger.error("Initialization failed.", t);
            throw new RuntimeException("Initialization failed.", t);
        }
    }

    public static Session getSession() {
        Session s = session.get();
        if (s == null) {
            s = sessionFactory.openSession();
            s.setFlushMode(FlushMode.COMMIT);
            s.setCacheMode(CacheMode.NORMAL);
            session.set(s);
            s.beginTransaction();
        }
        return s;
    }

    public static void commitSession() {
        Session s = session.get();
        if (s != null) {
            if (s.getTransaction() != null && s.getTransaction().isActive()) {
                boolean isDirty = s.isDirty();
                long startTime = System.currentTimeMillis();
                s.getTransaction().commit();
                if(isDirty) {
                    Statistics.dbCommitTime.add(System.currentTimeMillis() - startTime);
                }
                s.beginTransaction();
            }
        }
    }

    @SuppressWarnings("UnusedDeclaration")
    public static void rollbackSession() {
        Session s = session.get();
        if (s != null) {
            if (s.getTransaction() != null && s.getTransaction().isActive()) {
                s.getTransaction().rollback();
                s.beginTransaction();
            }
        }
    }

    public static void closeSession(boolean commit) {
        Session s = session.get();
        if (s != null) {
            try {
                if (s.getTransaction() != null && s.getTransaction().isActive()) {
                    if (commit) {
                        boolean isDirty = s.isDirty();
                        long startTime = System.currentTimeMillis();
                        s.getTransaction().commit();
                        if (isDirty) {
                            long time = System.currentTimeMillis() - startTime;
                            Statistics.dbCommitTime.add(time);
                            if (time > Time.SECOND_MILLIS * 3) {
                                logger.warn("Long commit: " + time + "ms");
                            }
                        }
                    } else {
                        s.getTransaction().rollback();
                    }
                }
            } finally {
                try {
                    s.close();
                } finally {
                    session.set(null);
                }
            }
        }
    }

    public static void persist(Object obj) {
        getSession().persist(obj);
    }

    public static void delete(Object obj) {
        getSession().delete(obj);
    }

    public static void flushSession() {
        Session s = session.get();
        if (s != null) {
            s.flush();
        }
    }

    public static void destroy() {
        logger.info("Closing...");
        sessionFactory.close();
        logger.info("Destroyed.");
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> clazz, int id) {
        return (T)getSession().get(clazz, id);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T> List<T> getAll(Class<T> clazz, List<Integer> ids) {
        List<T> result = new ArrayList<T>();
        for (int id : ids) {
            result.add((T)getSession().get(clazz, id));
        }
        return result;
    }
}
