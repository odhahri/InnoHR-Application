package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Team;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TeamDAO {

    public static void saveTeam(Team team) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(team);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static Team findTeamById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Team.class, id);
        }
    }


    public static List<Team> findAllTeams() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Team> query = session.createQuery("from Team", Team.class);
            return query.list();
        }
    }

    public static void updateTeam(Team team) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(team);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static void deleteTeam(Team team) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(team);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
