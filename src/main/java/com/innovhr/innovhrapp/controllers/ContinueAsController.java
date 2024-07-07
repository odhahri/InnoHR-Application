package com.innovhr.innovhrapp.controllers;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class ContinueAsController {

    @FXML
    private VBox roleSelectionBox;

    @FXML
    private Button adminButton;

    @FXML
    private Button managerButton;

    @FXML
    private Button collabButton;

    private UserNavigationHandler navigationHandler;

    public void initialize() {
        // Initialize the UserNavigationHandler with the session manager
        navigationHandler = new UserNavigationHandler(SessionManager.getInstance());

        // Get the logged-in user
        User loggedInUser = SessionManager.getInstance().getLoggedInUser();

        // Show the appropriate buttons based on the user's access level
        if (loggedInUser != null) {
            switch (loggedInUser.getAccessLevel()) {
                case ADMIN:
                    adminButton.setVisible(true);
                    managerButton.setVisible(true);
                    collabButton.setVisible(true);
                    break;
                case MANAGER:
                    managerButton.setVisible(true);
                    collabButton.setVisible(true);
                    break;
                case COLLAB:
                    collabButton.setVisible(true);
                    break;
                default:
                    // Handle unknown access levels if necessary
                    break;
            }
        }
    }

    @FXML
    private void handleAdminSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.ADMIN);
        navigationHandler.navigateToAdminSpace();
        System.out.println("Navigating to Admin Space...");
    }

    @FXML
    private void handleManagerSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.MANAGER);
        navigationHandler.navigateToManagerSpace();
        System.out.println("Navigating to Manager Space...");
    }

    @FXML
    private void handleCollabSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.COLLAB);
        navigationHandler.navigateToCollabSpace();
        System.out.println("Navigating to Collaborator Space...");
    }
}
