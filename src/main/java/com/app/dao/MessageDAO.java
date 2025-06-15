package com.app.dao;

import com.app.model.Model_Save_Key;
import com.app.model.Model_Save_Message;
import com.app.model.Model_Send_Message;
import com.app.util.HibernateUtil;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.hibernate.Length;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

public class MessageDAO {
    public CompletableFuture<Void> saveMessage(Model_Save_Message ms) {
        return CompletableFuture.runAsync(() -> {
            Transaction transaction = null;
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                transaction = session.beginTransaction();
                session.persist(ms);
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) {
                    HibernateUtil.rollbackTransaction(transaction);
                }
                throw new RuntimeException(e.getMessage(), e);
            }
        });
    }
    
    public CompletableFuture<List<Model_Save_Message>> getHistoryMessage(Long fromUserID, Long toUserID) {
        return CompletableFuture.supplyAsync(() -> {
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Transaction transaction = session.beginTransaction();
                try {
                    String hql = "FROM Model_Save_Message m "
                            + "WHERE (m.fromUserID = :fromUserID AND m.toUserID = :toUserID) "
                            + "OR (m.fromUserID = :toUserID AND m.toUserID = :fromUserID) ";
//                            + "ORDER BY m.mesage_id DESC";
                    Query<Model_Save_Message> query = session.createQuery(hql, Model_Save_Message.class);
                    query.setParameter("fromUserID", fromUserID);
                    query.setParameter("toUserID", toUserID);
                    List<Model_Save_Message> listMs = query.getResultList();
                    return listMs;
                    
                } catch (Exception e) {
                    System.out.println("aaaaaaaaaaaasffsddsfdf");
                    if (transaction != null && transaction.isActive()) {
                        transaction.rollback();
                    }
                    throw new RuntimeException("Lỗi khi tìm kiếm TestUserAccount: " + e.getMessage(), e);
                }
            }
        });
    }
}
