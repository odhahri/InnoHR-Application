package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Document;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DocumentDAO {

    // Save a new Document
    public static void saveDocument(Document document) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(document);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Find a Document by ID
    public static Document findDocumentById(int id) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            return session.get(Document.class, id);
        }
    }

    // Find a Document by label
    public static Document findDocumentByLabel(String label) {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            String hql = "FROM Document WHERE doc_label = :label";
            Query<Document> query = session.createQuery(hql, Document.class);
            query.setParameter("label", label);
            return query.uniqueResult();
        }
    }

    // Find all Documents
    public static List<Document> findAllDocuments() {
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            Query<Document> query = session.createQuery("from Document", Document.class);
            return query.list();
        }
    }

    // Update an existing Document
    public static void updateDocument(Document document) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(document);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Delete a Document
    public static void deleteDocument(Document document) {
        Transaction transaction = null;
        try (Session session = BDConnectivity.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.remove(document);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
