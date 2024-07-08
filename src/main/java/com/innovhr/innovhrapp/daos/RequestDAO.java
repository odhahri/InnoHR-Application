package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Request;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RequestDAO {

    public void saveRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Request findRequestById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Request.class, id);
        }
    }

    public List<Request> findAllRequests() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Request> query = session.createQuery("from Request", Request.class);
            return query.list();
        }
    }

    public void updateRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
