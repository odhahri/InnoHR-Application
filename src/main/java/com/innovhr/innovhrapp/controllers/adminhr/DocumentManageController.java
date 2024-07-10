package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.DocumentDAO;
import com.innovhr.innovhrapp.models.Document;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class DocumentManageController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private TableView<Document> documentsTable;
    @FXML
    private TableColumn<Document, Integer> docIdColumn;
    @FXML
    private TableColumn<Document, String> docLabelColumn;
    @FXML
    private TableColumn<Document, String> docTypeColumn;
    @FXML
    private Button addDocumentButton;
    @FXML
    private Button deleteDocumentButton;

    private final String pageName = "Document management";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public DocumentManageController() {
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
        addDocumentButton.setDisable(true);
        deleteDocumentButton.setDisable(true);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("Document Management Page");
        initializeColumns();
        loadDocuments();

        addDocumentButton.setOnAction(event -> addDocument());
        deleteDocumentButton.setOnAction(event -> deleteDocument());

        documentsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
                if (selectedDocument != null) {
                    saveAndOpenDocument(selectedDocument);
                }
            }
        });
    }

    private void initializeColumns() {
        docIdColumn.setCellValueFactory(new PropertyValueFactory<>("doc_id"));
        docLabelColumn.setCellValueFactory(new PropertyValueFactory<>("doc_label"));
        docTypeColumn.setCellValueFactory(new PropertyValueFactory<>("doc_type"));
    }

    private void loadDocuments() {
        List<Document> documentList = DocumentDAO.findAllDocuments();
        ObservableList<Document> documents = FXCollections.observableArrayList(documentList);
        documentsTable.setItems(documents);
    }

    private void addDocument() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Document");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.pdf"),
                new FileChooser.ExtensionFilter("Word Files", "*.docx"),
                new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                String fileType = getFileExtension(selectedFile);

                Document document = new Document();
                document.setDoc_label(selectedFile.getName());
                document.setDoc_type(fileType);
                document.setContent(fileContent);

                DocumentDAO.saveDocument(document);
                documentsTable.getItems().add(document);

                showAlert("Document Added", "Document added successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                showAlertError("Error", "Failed to add document. Please try again.");
            }
        }
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private void deleteDocument() {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Document");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this document?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                DocumentDAO.deleteDocument(selectedDocument);
                documentsTable.getItems().remove(selectedDocument);

                showAlert("Document Deleted", "Document deleted successfully!");
            }
        } else {
            showAlertError("Error", "No document selected.");
        }
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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
