package com.innovhr.innovhrapp.controllers.adminhr;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TeamManageController {
    @FXML
    private Label pageTitle;

    public void initialize() {
        pageTitle.setText("Teams management page");
    }
}
