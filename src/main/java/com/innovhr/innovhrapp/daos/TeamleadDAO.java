package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Teamlead;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class TeamleadDAO {

    public void saveTeamLead(Teamlead teamLead) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(teamLead);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Teamlead findTeamLeadById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Teamlead.class, id);
        }
    }

    public List<Teamlead> findAllTeamLeads() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Teamlead> query = session.createQuery("from Teamlead", Teamlead.class);
            return query.list();
        }
    }

    public void updateTeamLead(Teamlead teamLead) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(teamLead);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteTeamLead(Teamlead teamLead) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(teamLead);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
