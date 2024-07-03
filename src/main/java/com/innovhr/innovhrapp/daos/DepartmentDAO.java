package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Department;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class DepartmentDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveDepartment(Department department) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(department);
        em.getTransaction().commit();
        em.close();
    }

    public Department findDepartmentById(int id) {
        EntityManager em = emf.createEntityManager();
        Department department = em.find(Department.class, id);
        em.close();
        return department;
    }

    public List<Department> findAllDepartments() {
        EntityManager em = emf.createEntityManager();
        List<Department> departments = em.createQuery("from Department", Department.class).getResultList();
        em.close();
        return departments;
    }

    public void updateDepartment(Department department) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(department);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteDepartment(Department department) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        department = em.merge(department);
        em.remove(department);
        em.getTransaction().commit();
        em.close();
    }
}
