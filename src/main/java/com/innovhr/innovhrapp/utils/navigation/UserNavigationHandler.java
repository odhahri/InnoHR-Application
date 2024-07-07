package com.innovhr.innovhrapp.utils.navigation;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class UserNavigationHandler {

    private final SessionManager sessionManager;

    public UserNavigationHandler(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public void switchAccessLevel(User.AccessLevel newAccessLevel) {
        sessionManager.setDynamicAccessLevel(newAccessLevel);
        System.out.println("Switched to access level: " + newAccessLevel);
    }

    public void resetAccessLevel() {
        sessionManager.resetDynamicAccessLevel();
        System.out.println("Reset to primary access level: " + sessionManager.getLoggedInUser().getAccessLevel());
    }

    public void authorizePageAccess(String pageName, User.AccessLevel requiredAccessLevel) {
        User.AccessLevel currentAccessLevel = sessionManager.getDynamicAccessLevel();
        if (currentAccessLevel == requiredAccessLevel || currentAccessLevel == User.AccessLevel.ADMIN) {
            System.out.println("Access granted to page: " + pageName);
        } else {
            throw new UnsupportedOperationException("Access denied to page: " + pageName);
        }
    }

    public void authorizeAction(String actionName, User.AccessLevel requiredAccessLevel) {
        User.AccessLevel currentAccessLevel = sessionManager.getDynamicAccessLevel();
        if (currentAccessLevel == requiredAccessLevel || currentAccessLevel == User.AccessLevel.ADMIN) {
            System.out.println("Action authorized: " + actionName);
        } else {
            throw new UnsupportedOperationException("Unauthorized action: " + actionName);
        }
    }

    private void loadFXMLAndShow(String fxmlPath, String windowTitle) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void navigateToAdminSpace() {
        try {
            loadFXMLAndShow("/com/innovhr/innovhrapp/view/adminhr/adminhr-space.fxml", "Admin Space");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Admin Space", e);
        }
    }

    public void navigateToManagerSpace() {
        try {
            loadFXMLAndShow("/path/to/ManagerSpace.fxml", "Manager Space");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Manager Space", e);
        }
    }

    public void navigateToCollabSpace() {
        try {
            loadFXMLAndShow("/path/to/CollabSpace.fxml", "Collaborator Space");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Collaborator Space", e);
        }
    }
}
