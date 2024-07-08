package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Training;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TrainingDAO {

    public void saveTraining(Training training) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(training);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Training findTrainingById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Training.class, id);
        }
    }

    public static List<Training> findAllTrainings() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Training> query = session.createQuery("from Training", Training.class);
            return query.list();
        }
    }

    public void updateTraining(Training training) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(training);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteTraining(Training training) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(training);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
