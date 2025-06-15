package com.app.util;

import com.app.model.Model_Save_Key;
import com.app.model.Model_Save_Message;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HibernateUtil {
    private static final Logger logger = LoggerFactory.getLogger(HibernateUtil.class);
    private static volatile SessionFactory sessionFactory;

    private HibernateUtil() {
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory == null) {
                    try {
                        sessionFactory = new Configuration()
                                .configure("hibernate.cfg.xml")
                                .addAnnotatedClass(Model_Save_Message.class)
                                .addAnnotatedClass(Model_Save_Key.class)
                                .buildSessionFactory();
                        logger.info("SessionFactory created successfully");
                    } catch (Exception e) {
                        logger.error("Failed to create SessionFactory", e);
                        throw new RuntimeException("Hibernate SessionFactory creation failed", e);
                    }
                }
            }
        }
        return sessionFactory;
    }

    public static void close() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            synchronized (HibernateUtil.class) {
                if (sessionFactory != null && !sessionFactory.isClosed()) {
                    sessionFactory.close();
                    logger.info("SessionFactory closed");
                }
            }
        }
    }

    public static void rollbackTransaction(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            try {
                transaction.rollback();
                logger.warn("Transaction rolled back");
            } catch (Exception e) {
                logger.error("Failed to rollback transaction", e);
            }
        }
    }
}