package com.innovhr.innovhrapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class MainController {

    @FXML
    private ListView<String> menuListView;

    @FXML
    private AnchorPane mainContainer;

    @FXML
    public void initialize() {
        // Populate menu items
        menuListView.getItems().addAll("Profile", "Description");
    }

    @FXML
    public void handleMenuClick(MouseEvent event) {
        String selectedItem = menuListView.getSelectionModel().getSelectedItem();
        String fxmlFile = "";

        switch (selectedItem) {
            case "Profile":
                fxmlFile = "/com/innovhr/innovhrapp/view/profile-view.fxml";
                break;
            case "Description":
                fxmlFile = "/com/innovhr/innovhrapp/view/description-view.fxml";
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

            // You can also access the controller if needed
            if (fxmlFile.endsWith("profile-view.fxml")) {
                ProfileController controller = loader.getController();
                controller.initializeData("Hello, User!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
