package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Holiday;
import com.innovhr.innovhrapp.models.Request;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RequestDAO {

    public static void saveRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public static List<Object[]> findRequestsByStateWithEmployeeUsername(String state) {
        Transaction transaction = null;
        List<Object[]> requests = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String hql = "SELECT r, e.emp_id FROM Request r JOIN r.employee e WHERE r.request_state = :state";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            query.setParameter("state", state);
            requests = query.list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return requests;
    }

    public static List<Request> findRequestsByState(String state) {
        Transaction transaction = null;
        List<Request> requests = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requests = session.createQuery("FROM Request WHERE request_state = :state", Request.class)
                    .setParameter("state", state)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return requests;
    }

    public static List<Request> findRequestsByHoliday(Holiday holiday) {
        Transaction transaction = null;
        List<Request> requests = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            requests = session.createQuery("FROM Request WHERE holiday = :holiday", Request.class)
                    .setParameter("holiday", holiday)
                    .list();
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
        return requests;
    }

    public static List<Request> findRequestsByEmployee(int empId) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "FROM Request WHERE employee.emp_id = :empId";
            Query<Request> query = session.createQuery(hql, Request.class);
            query.setParameter("empId", empId);
            return query.list();
        }
    }

    public static boolean isDocumentAlreadyRequested(int empId, int docId) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "SELECT COUNT(*) FROM Request WHERE employee.emp_id = :empId AND document.doc_id = :docId";
            Query<Long> query = session.createQuery(hql, Long.class);
            query.setParameter("empId", empId);
            query.setParameter("docId", docId);
            return query.uniqueResult() > 0;
        }
    }
    public Request findRequestById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Request.class, id);
        }
    }

    public List<Request> findAllRequests() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Request> query = session.createQuery("from Request", Request.class);
            return query.list();
        }
    }

    public static void updateRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteRequest(Request request) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(request);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
