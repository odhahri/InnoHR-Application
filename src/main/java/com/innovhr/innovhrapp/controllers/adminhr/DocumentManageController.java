package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DocumentManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Documents management page");
    }
}
