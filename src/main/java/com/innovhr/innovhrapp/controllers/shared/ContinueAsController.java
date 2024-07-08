package com.innovhr.innovhrapp.controllers.shared;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
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

        // Initialize tooltips
        adminButton.setTooltip(new Tooltip("Access Admin Features"));
        managerButton.setTooltip(new Tooltip("Access Manager Features"));
        collabButton.setTooltip(new Tooltip("Access Collaborator Features"));

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
                    throw new UnsupportedOperationException("Unsupported access level");
            }
        }
    }

    @FXML
    private void handleAdminSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.ADMIN);
        navigationHandler.navigateToAdminSpace();
        FXMLViewLoader.closeScene(roleSelectionBox);
        System.out.println("Navigating to Admin Space...");
    }

    @FXML
    private void handleManagerSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.MANAGER);
        navigationHandler.navigateToManagerSpace();
        FXMLViewLoader.closeScene(roleSelectionBox);
        System.out.println("Navigating to Manager Space...");
    }

    @FXML
    private void handleCollabSelection() {
        navigationHandler.switchAccessLevel(User.AccessLevel.COLLAB);
        navigationHandler.navigateToCollabSpace();
        FXMLViewLoader.closeScene(roleSelectionBox);
        System.out.println("Navigating to Collaborator Space...");
    }
}
