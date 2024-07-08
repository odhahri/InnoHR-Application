package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Promotion;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class PromotionDAO {

    public void savePromotion(Promotion promotion) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(promotion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Promotion findPromotionById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Promotion.class, id);
        }
    }

    public List<Promotion> findAllPromotions() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Promotion> query = session.createQuery("from Promotion", Promotion.class);
            return query.list();
        }
    }

    public void updatePromotion(Promotion promotion) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(promotion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deletePromotion(Promotion promotion) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(promotion);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
