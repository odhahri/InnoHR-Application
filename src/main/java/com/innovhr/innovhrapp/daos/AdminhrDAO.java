package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Adminhr;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.Manager;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AdminhrDAO {

    public static void saveAdminhr(Adminhr adminhr) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(adminhr);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
    public static Adminhr findAdminhrByEmployee(Employee employee) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Adminhr> query = session.createQuery("from Adminhr where employee = :employee", Adminhr.class);
            query.setParameter("employee", employee);
            return query.uniqueResult();
        }
    }
    public static Adminhr findAdminhrById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Adminhr.class, id);
        }
    }

    public static List<Adminhr> findAllAdminhrs() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Adminhr> query = session.createQuery("from Adminhr", Adminhr.class);
            return query.list();
        }
    }

    public static void updateAdminhr(Adminhr adminhr) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(adminhr);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteAdminhr(Adminhr adminhr) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(adminhr);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
