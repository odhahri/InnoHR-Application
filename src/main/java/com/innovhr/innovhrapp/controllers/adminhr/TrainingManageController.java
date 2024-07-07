package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TrainingManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Training management page");
    }
}
