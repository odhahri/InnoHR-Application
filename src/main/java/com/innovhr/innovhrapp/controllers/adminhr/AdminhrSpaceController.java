package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.CustomCellFactory;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class AdminhrSpaceController implements AccessControlled {
    private final String pageName = "AdminhrSpace";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;

    @FXML
    private ListView<String> menuListView;

    @FXML
    private AnchorPane mainContainer;

    private final UserNavigationHandler navigationHandler;

    public AdminhrSpaceController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }

    @FXML
    public void initialize() {
        checkAccess();
        // Populate menu items
        menuListView.getItems().addAll("Employees", "Departments", "Manager", "Teams", "Documents", "Trainings", "Requests");
        menuListView.setCellFactory(listView -> new CustomCellFactory());
        menuListView.getSelectionModel().select("Employees");
        String fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_employee_path;
        loadView(fxmlFile);
    }

    @FXML
    public void handleMenuClick(MouseEvent event) {
        String selectedItem = menuListView.getSelectionModel().getSelectedItem();
        String fxmlFile = "";

        switch (selectedItem) {
            case "Employees":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_employee_path;
                break;
            case "Departments":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_department_path;
                break;
            case "Manager":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_manager_path;
                break;
            case "Teams":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_team_path;
                break;
            case "Documents":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_document_path;
                break;
            case "Trainings":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_training_path;
                break;
            case "Requests":
                fxmlFile = ViewPresets.AdminFxmlViews.fxml_admin_request_path;
                break;
            default:
                // Handle default case if needed
                break;
        }

        if (!fxmlFile.isEmpty()) {
            loadView(fxmlFile);
        }
    }

    private void loadView(String fxmlFile) {
        try {
            Object controller = FXMLViewLoader.loadSceneContainer(mainContainer,fxmlFile);
            // If the loaded controller implements AccessControlled, perform access check
            if (controller instanceof AccessControlled) {
                ((AccessControlled) controller).checkAccess();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            showAlertError("Access Denied", "You do not have permission to view this page.");
            disableComponents();
        }
    }

    private void disableComponents() {
        // Disable components to prevent unauthorized interactions
        menuListView.setDisable(true);
        mainContainer.setDisable(true);
    }
}
