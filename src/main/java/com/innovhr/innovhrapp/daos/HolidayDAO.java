package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Holiday;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HolidayDAO {

    public void saveHoliday(Holiday holiday) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(holiday);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Holiday findHolidayById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Holiday.class, id);
        }
    }

    public List<Holiday> findAllHolidays() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Holiday> query = session.createQuery("from Holiday", Holiday.class);
            return query.list();
        }
    }

    public void updateHoliday(Holiday holiday) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(holiday);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteHoliday(Holiday holiday) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(holiday);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
