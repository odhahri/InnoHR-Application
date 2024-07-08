package com.innovhr.innovhrapp;

import com.innovhr.innovhrapp.daos.UserDAO;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.database.BDConnectivity;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class InnovhrApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLViewLoader.loadLoginScene(stage, "Login",ViewPresets.SigninFXMLViews.fxml_login_path );
    }

    public static void main(String[] args) {
        BDConnectivity.makeSessionFactory();
        launch();
//        User AdminUser = new User();
//        AdminUser.setUsername("Oussama admin");
//        AdminUser.setPersonId(1);
//        AdminUser.setAccessLevel(User.AccessLevel.ADMIN);
//        AdminUser.setPassword("root");
//        UserDAO.saveUser(AdminUser);
    }
}