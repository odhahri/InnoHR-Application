package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Holiday;
import com.innovhr.innovhrapp.models.Team;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class HolidayDAO {

    public static List<Holiday> findHolidaysByTeamAndMonth(Team team, LocalDate startDate, LocalDate endDate) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "FROM Holiday WHERE employee.team = :team AND " +
                    "((:startDate BETWEEN holiday_start_date AND holiday_end_date) OR " +
                    "(:endDate BETWEEN holiday_start_date AND holiday_end_date) OR " +
                    "(holiday_start_date BETWEEN :startDate AND :endDate) OR " +
                    "(holiday_end_date BETWEEN :startDate AND :endDate))";
            Query<Holiday> query = session.createQuery(hql, Holiday.class);
            query.setParameter("team", team);
            query.setParameter("startDate", java.sql.Date.valueOf(startDate));
            query.setParameter("endDate", java.sql.Date.valueOf(endDate));
            return query.list();
        }
    }
    public static void saveHoliday(Holiday holiday) {
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

    public static Holiday findHolidayById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Holiday.class, id);
        }
    }

    public static List<Holiday> findAllHolidays() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Holiday> query = session.createQuery("from Holiday", Holiday.class);
            return query.list();
        }
    }

    public static boolean isHolidayOverlap(int employeeId, LocalDate startDate, LocalDate endDate) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM Holiday WHERE employee.emp_id = :employeeId AND " +
                    "((:startDate BETWEEN holiday_start_date AND holiday_end_date) OR " +
                    "(:endDate BETWEEN holiday_start_date AND holiday_end_date) OR " +
                    "(holiday_start_date BETWEEN :startDate AND :endDate) OR " +
                    "(holiday_end_date BETWEEN :startDate AND :endDate))";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("employeeId", employeeId);
            query.setParameter("startDate", java.sql.Date.valueOf(startDate));
            query.setParameter("endDate", java.sql.Date.valueOf(endDate));
            return query.uniqueResult() > 0;
        }
    }

    public static void updateHoliday(Holiday holiday) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(holiday);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteHoliday(Holiday holiday) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(holiday);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
