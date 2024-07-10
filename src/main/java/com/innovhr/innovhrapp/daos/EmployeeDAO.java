package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class EmployeeDAO {

    public static void saveEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(employee); // Using persist instead of save
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public static Employee findEmployeeById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Employee.class, id);
        }
    }

    public List<Employee> findAllEmployees() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Employee> query = session.createQuery("from Employee", Employee.class);
            return query.list();
        }
    }


    public static void updateEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(employee); // Using merge instead of update
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public static void deleteEmployee(Employee employee) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(employee); // Using remove instead of delete
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public static Employee findEmployeeByUsername(String username) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "FROM Employee WHERE emp_username = :username";
            Query<Employee> query = session.createQuery(hql, Employee.class);
            query.setParameter("username", username);
            return query.uniqueResult();
        }
    }



}
