package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Department;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DepartmentDAO {

    public void saveDepartment(Department department) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Department findDepartmentById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Department.class, id);
        }
    }

    public static List<Department> findAllDepartments() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Department> query = session.createQuery("from Department", Department.class);
            return query.list();
        }
    }

    public void updateDepartment(Department department) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteDepartment(Department department) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(department);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
