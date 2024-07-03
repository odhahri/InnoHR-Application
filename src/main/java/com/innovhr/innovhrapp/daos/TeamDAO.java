package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Team;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class TeamDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveTeam(Team team) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(team);
        em.getTransaction().commit();
        em.close();
    }

    public Team findTeamById(int id) {
        EntityManager em = emf.createEntityManager();
        Team team = em.find(Team.class, id);
        em.close();
        return team;
    }

    public List<Team> findAllTeams() {
        EntityManager em = emf.createEntityManager();
        List<Team> teams = em.createQuery("from Team", Team.class).getResultList();
        em.close();
        return teams;
    }

    public void updateTeam(Team team) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(team);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteTeam(Team team) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        team = em.merge(team);
        em.remove(team);
        em.getTransaction().commit();
        em.close();
    }
}
