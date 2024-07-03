package com.innovhr.innovhrapp.daos;

import com.innovhr.innovhrapp.models.Request;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;

public class RequestDAO {
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("InnovHRApp");

    public void saveRequest(Request request) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(request);
        em.getTransaction().commit();
        em.close();
    }

    public Request findRequestById(int id) {
        EntityManager em = emf.createEntityManager();
        Request request = em.find(Request.class, id);
        em.close();
        return request;
    }

    public List<Request> findAllRequests() {
        EntityManager em = emf.createEntityManager();
        List<Request> requests = em.createQuery("from Request", Request.class).getResultList();
        em.close();
        return requests;
    }

    public void updateRequest(Request request) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(request);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteRequest(Request request) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        request = em.merge(request);
        em.remove(request);
        em.getTransaction().commit();
        em.close();
    }
}
