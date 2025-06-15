package com.app.dao;

import com.app.model.Model_Save_Key;
import com.app.util.HibernateUtil;
import java.util.concurrent.CompletableFuture;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class KeyDAO {
    public CompletableFuture<Void> addKey(Model_Save_Key key) {
        return CompletableFuture.runAsync(() -> {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(key);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    HibernateUtil.rollbackTransaction(transaction);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
    
    public CompletableFuture<Model_Save_Key> addAndgetKey(Model_Save_Key key) {
        return CompletableFuture.supplyAsync(() -> {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(key);
                session.flush();
                transaction.commit();
                return session.get(Model_Save_Key.class, key.getId());
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    HibernateUtil.rollbackTransaction(transaction);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
    
    public CompletableFuture<Model_Save_Key> getKey(Long userID) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    String hql = "FROM Model_Save_Key m WHERE m.userID = :userID";
                    Query<Model_Save_Key> query = session.createQuery(hql, Model_Save_Key.class);
                    query.setParameter("userID", userID);
                    query.setMaxResults(1); 
                    Model_Save_Key key = query.uniqueResult();
                    transaction.commit();
                    return key;
                } catch (Exception e) {
                    if (transaction != null && transaction.isActive()) {
                        transaction.rollback();
                    }
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
        });
    }
}
