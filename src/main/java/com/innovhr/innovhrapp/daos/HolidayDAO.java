package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Holiday;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class HolidayDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveHoliday(Holiday holiday) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(holiday);
        em.getTransaction().commit();
        em.close();
    }

    public Holiday findHolidayById(int id) {
        EntityManager em = emf.createEntityManager();
        Holiday holiday = em.find(Holiday.class, id);
        em.close();
        return holiday;
    }

    public List<Holiday> findAllHolidays() {
        EntityManager em = emf.createEntityManager();
        List<Holiday> holidays = em.createQuery("from Holiday", Holiday.class).getResultList();
        em.close();
        return holidays;
    }

    public void updateHoliday(Holiday holiday) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(holiday);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteHoliday(Holiday holiday) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        holiday = em.merge(holiday);
        em.remove(holiday);
        em.getTransaction().commit();
        em.close();
    }
}
