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
    requires jakarta.validation;
    requires java.desktop;
    opens com.innovhr.innovhrapp to javafx.fxml;
    exports com.innovhr.innovhrapp.models;
    opens com.innovhr.innovhrapp.models to  org.hibernate.orm.core;
    exports com.innovhr.innovhrapp;
    exports com.innovhr.innovhrapp.controllers;
    exports com.innovhr.innovhrapp.controllers.adminhr;
    opens com.innovhr.innovhrapp.controllers to javafx.fxml;
    opens com.innovhr.innovhrapp.controllers.adminhr to javafx.fxml;
    exports com.innovhr.innovhrapp.controllers.shared;
    exports com.innovhr.innovhrapp.controllers.collabhr ;
    opens com.innovhr.innovhrapp.controllers.shared to javafx.fxml;
    opens com.innovhr.innovhrapp.controllers.collabhr to javafx.fxml;
}