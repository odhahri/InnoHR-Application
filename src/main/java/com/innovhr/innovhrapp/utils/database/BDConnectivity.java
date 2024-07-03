package com.innovhr.innovhrapp.utils.database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class BDConnectivity {

    private static SessionFactory sessionFactory;

    // Private constructor to prevent instantiation
    public BDConnectivity() {}

    // Initialize the SessionFactory from the Hibernate configuration file
    public static void makeSessionFactory() {
        try {
            // Create the Configuration instance, apply settings from hibernate.cfg.xml
            Configuration configuration = new Configuration().configure();
            // Build the StandardServiceRegistry
            StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties());
            // Create the SessionFactory from hibernate.cfg.xml
            sessionFactory = new Configuration().configure().buildSessionFactory();
            System.out.println("Initial SessionFactory creation success." );
        } catch (Throwable ex) {
            // Make sure you log the exception to track it in production
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    // Get the SessionFactory
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    // Close caches and connection pools
    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
