package com.innovhr.innovhrapp.utils.navigation;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
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
        User.AccessLevel currentLevel = sessionManager.getInstance().getLoggedInUser().getAccessLevel();
        if (currentLevel == User.AccessLevel.ADMIN) {
            sessionManager.setDynamicAccessLevel(newAccessLevel);
        } else if (currentLevel == User.AccessLevel.MANAGER && newAccessLevel != User.AccessLevel.ADMIN) {
            sessionManager.setDynamicAccessLevel(newAccessLevel);
        } else if (currentLevel == User.AccessLevel.COLLAB && newAccessLevel == User.AccessLevel.COLLAB) {
            sessionManager.setDynamicAccessLevel(newAccessLevel);
        } else {
            throw new UnsupportedOperationException("Permission denied to switch to the specified access level");
        }
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


    public void navigateToAdminSpace() {
        try {
            FXMLViewLoader.loadScene("Admin Space",ViewPresets.AdminFxmlViews.fxml_admin_space_path );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Admin Space", e);
        }
    }

    public void navigateToManagerSpace() {
        try {
            FXMLViewLoader.loadScene("Manager Space",ViewPresets.ManagerFxmlViews.fxml_manager_space_path );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Manager Space", e);
        }
    }

    public void navigateToCollabSpace() {
        try {
            FXMLViewLoader.loadScene("Collaborator Space",ViewPresets.CollabFxmlViews.fxml_collab_space_path );
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load Collaborator Space", e);
        }
    }
}
