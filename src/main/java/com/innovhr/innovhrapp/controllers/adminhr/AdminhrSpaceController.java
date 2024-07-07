package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.controllers.ProfileController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class AdminhrSpaceController {
    @FXML
    private ListView<String> menuListView;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    public void initialize() {
        // Populate menu items

        menuListView.getItems().addAll("Employees", "Departments", "Manager", "Teams ", "Documents", "Trainings","Requests");
        menuListView.getSelectionModel().select("Employees");
        String fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/employee-manage.fxml";
        loadView(fxmlFile);
    }

    @FXML
    public void handleMenuClick(MouseEvent event) {
        String selectedItem = menuListView.getSelectionModel().getSelectedItem();
        String fxmlFile = "";

        switch (selectedItem) {
            case "Employees":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/employee-manage.fxml";
                break;
            case "Departments":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/department-manage.fxml";
                break;
            case "Manager":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/manager-manage.fxml";
                break;
            case "Teams":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/team-manage.fxml";
                break;
            case "Documents":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/document-manage.fxml";
                break;
            case "Trainings":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/training-manage.fxml";
                break;
            case "Requests":
                fxmlFile = "/com/innovhr/innovhrapp/view/adminhr/request-manage.fxml";
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Node view = loader.load();
            mainContainer.getChildren().setAll(view);
            Object controller = loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
