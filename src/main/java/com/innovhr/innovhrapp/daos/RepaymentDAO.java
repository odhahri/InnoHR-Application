package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Repayment;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepaymentDAO {

    public static void saveRepayment(Repayment repayment) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(repayment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Repayment findRepaymentById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Repayment.class, id);
        }
    }

    public static List<Repayment> findAllRepayments() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Repayment> query = session.createQuery("from Repayment", Repayment.class);
            return query.list();
        }
    }

    public static void updateRepayment(Repayment repayment) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(repayment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteRepayment(Repayment repayment) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(repayment);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
