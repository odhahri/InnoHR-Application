package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Promotion;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class PromotionDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void savePromotion(Promotion promotion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(promotion);
        em.getTransaction().commit();
        em.close();
    }

    public Promotion findPromotionById(int id) {
        EntityManager em = emf.createEntityManager();
        Promotion promotion = em.find(Promotion.class, id);
        em.close();
        return promotion;
    }

    public List<Promotion> findAllPromotions() {
        EntityManager em = emf.createEntityManager();
        List<Promotion> promotions = em.createQuery("from Promotion", Promotion.class).getResultList();
        em.close();
        return promotions;
    }

    public void updatePromotion(Promotion promotion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(promotion);
        em.getTransaction().commit();
        em.close();
    }

    public void deletePromotion(Promotion promotion) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        promotion = em.merge(promotion);
        em.remove(promotion);
        em.getTransaction().commit();
        em.close();
    }
}
