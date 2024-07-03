package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Teamlead;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class TeamleadDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveTeamlead(Teamlead teamlead) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(teamlead);
        em.getTransaction().commit();
        em.close();
    }

    public Teamlead findTeamleadById(int id) {
        EntityManager em = emf.createEntityManager();
        Teamlead teamlead = em.find(Teamlead.class, id);
        em.close();
        return teamlead;
    }

    public List<Teamlead> findAllTeamleads() {
        EntityManager em = emf.createEntityManager();
        List<Teamlead> teamleads = em.createQuery("from Teamlead", Teamlead.class).getResultList();
        em.close();
        return teamleads;
    }

    public void updateTeamlead(Teamlead teamlead) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(teamlead);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteTeamlead(Teamlead teamlead) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        teamlead = em.merge(teamlead);
        em.remove(teamlead);
        em.getTransaction().commit();
        em.close();
    }
}
