package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RequestManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Requests management page");
    }
}
