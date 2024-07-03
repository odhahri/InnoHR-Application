package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Training;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class TrainingDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveTraining(Training training) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(training);
        em.getTransaction().commit();
        em.close();
    }

    public Training findTrainingById(int id) {
        EntityManager em = emf.createEntityManager();
        Training training = em.find(Training.class, id);
        em.close();
        return training;
    }

    public List<Training> findAllTrainings() {
        EntityManager em = emf.createEntityManager();
        List<Training> trainings = em.createQuery("from Training", Training.class).getResultList();
        em.close();
        return trainings;
    }

    public void updateTraining(Training training) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(training);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteTraining(Training training) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        training = em.merge(training);
        em.remove(training);
        em.getTransaction().commit();
        em.close();
    }
}
