package com.innovhr.innovhrapp.utils.component;


import javafx.scene.control.ListCell;

public class CustomCellFactory extends ListCell<String> {
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(item);
            setStyle("-fx-padding: 10px; -fx-font-size: 16px;");
        }
    }
}
