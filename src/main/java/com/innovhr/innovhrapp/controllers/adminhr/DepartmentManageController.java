package com.innovhr.innovhrapp.controllers.adminhr;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DepartmentManageController implements AccessControlled {
    private final String pageName = "DepartmentManage";
    private final  User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    @FXML
    private Label pageTitle;
    private final UserNavigationHandler navigationHandler;

    public DepartmentManageController(){
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());

    }
    public void initialize() {
        pageTitle.setText("Departments management page");
    }
    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            AlertUtils.showAlertError("Access Denied", "You do not have permission to view this page.");
            //disableComponents();

            throw e;

        }
    }
}
