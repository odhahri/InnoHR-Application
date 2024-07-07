package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DepartmentManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Departments management page");
    }
}
