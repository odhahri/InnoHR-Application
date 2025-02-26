package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Salary;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class SalaryDAO {

    public void saveSalary(Salary salary) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(salary);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Salary findSalaryById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Salary.class, id);
        }
    }

    public List<Salary> findAllSalaries() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Salary> query = session.createQuery("from Salary", Salary.class);
            return query.list();
        }
    }

    public void updateSalary(Salary salary) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(salary);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteSalary(Salary salary) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(salary);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
