module com.innovhr.innovhrapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.persistence;
    requires jakarta.persistence;
    opens com.innovhr.innovhrapp to javafx.fxml;
    exports com.innovhr.innovhrapp.models;
    opens com.innovhr.innovhrapp.models to  org.hibernate.orm.core;
    exports com.innovhr.innovhrapp;
    exports com.innovhr.innovhrapp.controllers;
    opens com.innovhr.innovhrapp.controllers to javafx.fxml;
}