package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.DepartmentDAO;
import com.innovhr.innovhrapp.models.Department;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.util.List;

public class DepartmentManageController implements AccessControlled {
    private final String pageName = "DepartmentManage";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;

    @FXML
    private Label pageTitle;
    @FXML
    private TextField depIdField;
    @FXML
    private TextField depNameField;
    @FXML
    private TextField depDescriptionField;
    @FXML
    private TextField depEmailField;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private TableView<Department> departmentTable;

    private final UserNavigationHandler navigationHandler;

    public DepartmentManageController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }

    public void initialize() {
        pageTitle.setText("Departments Management Page");
        initializeColumns();
        loadDepartments();
        depIdField.setVisible(true);
        depIdField.setEditable(false);
        depIdField.setDisable(true);
        saveButton.setOnAction(event -> saveDepartment());
        deleteButton.setOnAction(event -> deleteDepartment());
        clearButton.setOnAction(event -> clearFields());

        departmentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> populateDepartmentData(newSelection));
    }

    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            AlertUtils.showAlertError("Access Denied", "You do not have permission to view this page.");
            disableComponents();
        }
    }

    private void disableComponents() {
        setFieldsDisabled(true);
    }

    private void setFieldsDisabled(boolean disabled) {
        depNameField.setDisable(disabled);
        depDescriptionField.setDisable(disabled);
        depEmailField.setDisable(disabled);
        saveButton.setDisable(disabled);
        deleteButton.setDisable(disabled);
    }

    private void loadDepartments() {
        List<Department> departments = DepartmentDAO.findAllDepartments();
        ObservableList<Department> departmentList = FXCollections.observableArrayList(departments);
        departmentTable.setItems(departmentList);
    }

    private void initializeColumns() {
        TableColumn<Department, Integer> depIdColumn = new TableColumn<>("ID");
        depIdColumn.setCellValueFactory(new PropertyValueFactory<>("dep_id"));

        TableColumn<Department, String> depNameColumn = new TableColumn<>("Name");
        depNameColumn.setCellValueFactory(new PropertyValueFactory<>("dep_name"));

        TableColumn<Department, String> depDescriptionColumn = new TableColumn<>("Description");
        depDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("dep_description"));

        TableColumn<Department, String> depEmailColumn = new TableColumn<>("Email");
        depEmailColumn.setCellValueFactory(new PropertyValueFactory<>("dep_email"));

        departmentTable.getColumns().setAll(depIdColumn, depNameColumn, depDescriptionColumn, depEmailColumn);
    }

    private void populateDepartmentData(Department department) {
        if (department != null) {
            depIdField.setText(String.valueOf(department.getDep_id()));
            depNameField.setText(department.getDep_name());
            depDescriptionField.setText(department.getDep_description());
            depEmailField.setText(department.getDep_email());
            setFieldsDisabled(false);
        }
    }

    private void clearFields() {
        if (!depIdField.getText().isEmpty() && !depNameField.getText().isEmpty() && !depDescriptionField.getText().isEmpty() && !depEmailField.getText().isEmpty()){
            depIdField.clear();
        }
        depNameField.clear();
        depDescriptionField.clear();
        depEmailField.clear();
        setFieldsDisabled(false);
    }

    private void saveDepartment() {
        try {
            String name = depNameField.getText();
            String description = depDescriptionField.getText();
            String email = depEmailField.getText();

            if (name.isEmpty() || description.isEmpty() || email.isEmpty()) {
                AlertUtils.showAlertError("Error", "All fields are required.");
                return;
            }

            Department department;
            if (depIdField.getText().isEmpty()) {
                // New department
                department = new Department();
                department.setDep_name(name);
                department.setDep_description(description);
                department.setDep_email(email);
                DepartmentDAO.saveDepartment(department);
                AlertUtils.showAlertSuccess("Success", "Department added successfully.");
            } else {
                // Existing department
                int departmentId = Integer.parseInt(depIdField.getText());
                department = DepartmentDAO.findDepartmentById(departmentId);
                if (department == null) {
                    AlertUtils.showAlertError("Error", "Department not found.");
                    return;
                }

                department.setDep_name(name);
                department.setDep_description(description);
                department.setDep_email(email);
                DepartmentDAO.updateDepartment(department);
                AlertUtils.showAlertSuccess("Success", "Department updated successfully.");
            }

            loadDepartments();
            clearFields();
            //setFieldsDisabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showAlertError("Error", "Failed to save department. Please try again.");
        }
    }

    private void deleteDepartment() {
        if (depIdField.getText().isEmpty()) {
            AlertUtils.showAlertError("Error", "Department ID is required.");
            return;
        } else {
            int departmentId = Integer.parseInt(depIdField.getText());
            Department department = DepartmentDAO.findDepartmentById(departmentId);
            if (department != null) {
                DepartmentDAO.deleteDepartment(department);
                AlertUtils.showAlertSuccess("Department deleted", "Department deleted successfully");
                loadDepartments();
                clearFields();
                setFieldsDisabled(false);
            } else {
                AlertUtils.showAlertError("Department not found", "No department found with ID: " + departmentId);
            }
        }
    }
}
