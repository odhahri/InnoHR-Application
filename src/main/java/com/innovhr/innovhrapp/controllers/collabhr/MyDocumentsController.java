package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.models.Document;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyDocumentsController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private TableView<Document> documentsTable;
    @FXML
    private TableColumn<Document, Integer> docIdColumn;
    @FXML
    private TableColumn<Document, String> docLabelColumn;
    @FXML
    private TableColumn<Document, String> docDescriptionColumn;
    @FXML
    private TableColumn<Document, String> docTypeColumn;
    @FXML
    private TableColumn<Document, String> docCreatedDateColumn;

    private final String pageName = "My Documents";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;

    public MyDocumentsController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }

    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            showAlertError("Access Denied", "You do not have permission to view this page.");
            disableComponents();
        }
    }

    private void disableComponents() {
        documentsTable.setDisable(true);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("My Documents");
        initializeColumns();
        loadDocuments();

        documentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
                if (selectedDocument != null) {
                    saveAndOpenDocument(selectedDocument);
                }
            }
        });
    }

    private void saveAndOpenDocument(Document document) {
        try {
            File tempFile = File.createTempFile(document.getDoc_label(), "." + document.getDoc_type());
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(document.getContent());
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                showAlertError("Error", "Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlertError("Error", "Failed to open the document.");
        }
    }

    private void initializeColumns() {
        docLabelColumn.setCellValueFactory(new PropertyValueFactory<>("doc_label"));
        docTypeColumn.setCellValueFactory(new PropertyValueFactory<>("doc_type"));
    }

    private void loadDocuments() {
        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        List<Document> documentList = employee.getDocuments();
        ObservableList<Document> documents = FXCollections.observableArrayList(documentList);
        documentsTable.setItems(documents);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
