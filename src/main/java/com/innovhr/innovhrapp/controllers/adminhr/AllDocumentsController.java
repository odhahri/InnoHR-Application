package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.models.Document;
import com.innovhr.innovhrapp.daos.DocumentDAO;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class AllDocumentsController implements AccessControlled {

    @FXML
    private TableView<Document> allDocumentsTable;


    private EmployeeManageController parentController;
    private final String pageName = "All Documents table";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public AllDocumentsController(){
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
        // Disable components to prevent unauthorized interactions
        allDocumentsTable.setDisable(true);
    }
    @FXML
    public void initialize() {
        // Populate the table with all documents
        ObservableList<Document> documents = FXCollections.observableArrayList(DocumentDAO.findAllDocuments());
        allDocumentsTable.setItems(documents);
    }

    public void setParentController(EmployeeManageController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void selectDocument() {
        Document selectedDocument = allDocumentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null && parentController != null) {
            parentController.addDocumentToTable(selectedDocument);
            Stage stage = (Stage) allDocumentsTable.getScene().getWindow();
            stage.close();
        }
    }


}
