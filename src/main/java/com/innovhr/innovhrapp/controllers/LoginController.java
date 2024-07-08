package com.innovhr.innovhrapp.controllers;

import com.innovhr.innovhrapp.daos.UserDAO;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {


    public VBox loginBox;
    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private UserDAO userDAO;

    public LoginController() {
        this.userDAO = new UserDAO();
    }

    @FXML
    private void handleLoginAction() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        User user = userDAO.findUserByUsername(username);
        if (user != null && checkPassword(password, user.getPassword())) {
            SessionManager.getInstance().setLoggedInUser(user);
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome " + username);
            loadMainApp();
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
        }
    }

    private boolean checkPassword(String inputPassword, String storedHashedPassword) {
        String hashedInputPassword = Integer.toHexString(inputPassword.hashCode());
        return hashedInputPassword.equals(storedHashedPassword);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadMainApp() {
        try {
//            // Load the "Continue As" screen
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/innovhr/innovhrapp/view/shared/continue-as.fxml"));
//            Parent root = loader.load();
//
//            // Set up a new stage for the main application window
//            Stage stage = new Stage();
//            stage.setTitle("Continue As");
//            stage.setScene(new javafx.scene.Scene(root));
//            stage.show();
            FXMLViewLoader.loadScene("Continue as", ViewPresets.SharedFXMLViews.fxml_continue_as_path );
            // Close the login window
            FXMLViewLoader.closeScene(loginBox);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load the main application: " + e.getMessage());
        }
    }
}
