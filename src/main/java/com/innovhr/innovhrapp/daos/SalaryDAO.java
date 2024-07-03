package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Salary;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class SalaryDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveSalary(Salary salary) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(salary);
        em.getTransaction().commit();
        em.close();
    }

    public Salary findSalaryById(int id) {
        EntityManager em = emf.createEntityManager();
        Salary salary = em.find(Salary.class, id);
        em.close();
        return salary;
    }

    public List<Salary> findAllSalaries() {
        EntityManager em = emf.createEntityManager();
        List<Salary> salaries = em.createQuery("from Salary", Salary.class).getResultList();
        em.close();
        return salaries;
    }

    public void updateSalary(Salary salary) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(salary);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteSalary(Salary salary) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        salary = em.merge(salary);
        em.remove(salary);
        em.getTransaction().commit();
        em.close();
    }
}
