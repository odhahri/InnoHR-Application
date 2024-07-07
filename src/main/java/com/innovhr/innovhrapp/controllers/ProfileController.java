package com.innovhr.innovhrapp.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class ProfileController {

    @FXML
    private Label helloLabel;

    public void initializeData(String message) {
        helloLabel.setText(message);
    }
}
