package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EmployeeManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Employee management page");
    }
}
