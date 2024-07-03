package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Employee;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class EmployeeDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveEmployee(Employee employee) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(employee);
        em.getTransaction().commit();
        em.close();
    }

    public Employee findEmployeeById(int id) {
        EntityManager em = emf.createEntityManager();
        Employee employee = em.find(Employee.class, id);
        em.close();
        return employee;
    }

    public List<Employee> findAllEmployees() {
        EntityManager em = emf.createEntityManager();
        List<Employee> employees = em.createQuery("from Employee", Employee.class).getResultList();
        em.close();
        return employees;
    }

    public void updateEmployee(Employee employee) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(employee);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteEmployee(Employee employee) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        employee = em.merge(employee);
        em.remove(employee);
        em.getTransaction().commit();
        em.close();
    }
}
