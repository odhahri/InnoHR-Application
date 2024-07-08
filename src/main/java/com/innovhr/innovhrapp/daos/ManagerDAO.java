package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Manager;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ManagerDAO {

    public void saveManager(Manager manager) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(manager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Manager findManagerById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Manager.class, id);
        }
    }

    public static List<Manager> findAllManagers() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Manager> query = session.createQuery("from Manager", Manager.class);
            return query.list();
        }
    }

    public void updateManager(Manager manager) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(manager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteManager(Manager manager) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(manager);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
