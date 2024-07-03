package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Manager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class ManagerDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveManager(Manager manager) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(manager);
        em.getTransaction().commit();
        em.close();
    }

    public Manager findManagerById(int id) {
        EntityManager em = emf.createEntityManager();
        Manager manager = em.find(Manager.class, id);
        em.close();
        return manager;
    }

    public List<Manager> findAllManagers() {
        EntityManager em = emf.createEntityManager();
        List<Manager> managers = em.createQuery("from Manager", Manager.class).getResultList();
        em.close();
        return managers;
    }

    public void updateManager(Manager manager) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(manager);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteManager(Manager manager) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        manager = em.merge(manager);
        em.remove(manager);
        em.getTransaction().commit();
        em.close();
    }
}
