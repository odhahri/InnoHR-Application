package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.DepartmentDAO;
import com.innovhr.innovhrapp.daos.EmployeeDAO;
import com.innovhr.innovhrapp.daos.ManagerDAO;
import com.innovhr.innovhrapp.daos.UserDAO;
import com.innovhr.innovhrapp.models.Department;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.Manager;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class ManagerManageController implements AccessControlled {
    @FXML
    private Label pageTitle;
    @FXML
    private TextField managerIdField;
    @FXML
    private TextField managerNameField;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private TableView<Manager> managerTable;

    private final String pageName = "Manager management";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public ManagerManageController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
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
        managerNameField.setDisable(disabled);
        departmentComboBox.setDisable(disabled);
        saveButton.setDisable(disabled);
        deleteButton.setDisable(disabled);
        clearButton.setDisable(disabled);
        managerTable.setDisable(disabled);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("Managers Management Page");
        initializeColumns();
        loadManagers();
        managerIdField.setVisible(true);
        managerIdField.setEditable(false);
        managerIdField.setDisable(true);
        saveButton.setOnAction(event -> saveManager());
        deleteButton.setOnAction(event -> deleteManager());
        clearButton.setOnAction(event -> clearFields());
        populateComboBoxes();
        managerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> populateManagerData(newSelection));
    }

    private void loadManagers() {
        List<Manager> managers = ManagerDAO.findAllManagers();
        ObservableList<Manager> managerList = FXCollections.observableArrayList(managers);
        managerTable.setItems(managerList);
    }

    private void initializeColumns() {
        TableColumn<Manager, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Manager, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Manager, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getDep_name()));

        managerTable.getColumns().setAll(idColumn, nameColumn, departmentColumn);
    }

    private void populateManagerData(Manager manager) {
        if (manager != null) {
            managerIdField.setText(String.valueOf(manager.getId()));
            managerNameField.setText(manager.getName());
            departmentComboBox.setValue(manager.getDepartment().getDep_id() + " | " + manager.getDepartment().getDep_name());
            setFieldsDisabled(false);
        }
    }

    private void clearFields() {
        if (!managerIdField.getText().isEmpty() && !managerNameField.getText().isEmpty()) {
            managerIdField.clear();
        }
        managerNameField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        setFieldsDisabled(false);
    }

    private void saveManager() {
        try {
            String name = managerNameField.getText();
            String departmentString = departmentComboBox.getValue();

            if (name.isEmpty() || departmentString == null) {
                AlertUtils.showAlertError("Error", "All fields are required.");
                return;
            }

            int departmentId = Integer.parseInt(departmentString.split(" \\| ")[0]);
            Department department = DepartmentDAO.findDepartmentById(departmentId);

            if (department == null) {
                AlertUtils.showAlertError("Error", "Invalid department selection.");
                return;
            }

            Manager manager;
            if (managerIdField.getText().isEmpty()) {
                // New manager

                // We start creating employee associated to the manager
                try {
                    Employee employee = new Employee();
                    employee.setEmp_username(name);
                    employee.setDepartment(department);
                    employee.setManager(null);
                    employee.setTeam(null);
                    EmployeeDAO.saveEmployee(employee);
                    employee = EmployeeDAO.findEmployeeByUsername(employee.getEmp_username());
                    manager = new Manager();
                    manager.setName(name);
                    manager.setDepartment(department);
                    manager.setEmployee(employee);
                    ManagerDAO.saveManager(manager);
                    User user = new User();
                    user.setEmployee(employee);
                    user.setAccessLevel(User.AccessLevel.MANAGER);
                    user.setUsername(name);
                    user.setPassword(name+"-InnovhrManager");
                    UserDAO.saveUser(user);
                }catch (Exception e) {
                    AlertUtils.showAlertError("Error", e.getMessage());
                }
                AlertUtils.showAlertSuccess("Success", "Manager added successfully.");
            } else {
                // Existing manager
                int managerId = Integer.parseInt(managerIdField.getText());
                manager = ManagerDAO.findManagerById(managerId);
                if (manager == null) {
                    AlertUtils.showAlertError("Error", "Manager not found.");
                    return;
                }
                try{
                    Employee employee = manager.getEmployee();
                    employee.setEmp_username(name);
                    employee.setDepartment(department);
                    EmployeeDAO.saveEmployee(employee);
                    manager.setName(name);
                    manager.setDepartment(department);
                    ManagerDAO.updateManager(manager);
                    AlertUtils.showAlertSuccess("Success", "Manager updated successfully.");
                }catch (Exception e) {
                    AlertUtils.showAlertError("Error", e.getMessage());
                }

            }

            loadManagers();
            clearFields();
        } catch (Exception e) {
            e.printStackTrace();
            AlertUtils.showAlertError("Error", "Failed to save manager. Please try again.");
        }
    }

    private void deleteManager() {
        if (managerIdField.getText().isEmpty()) {
            AlertUtils.showAlertError("Error", "Manager ID is required.");
            return;
        } else {
            int managerId = Integer.parseInt(managerIdField.getText());
            Manager manager = ManagerDAO.findManagerById(managerId);
            if (manager != null) {
                ManagerDAO.deleteManager(manager);
                AlertUtils.showAlertSuccess("Manager deleted", "Manager deleted successfully");
                loadManagers();
                clearFields();
                setFieldsDisabled(false);
            } else {
                AlertUtils.showAlertError("Manager not found", "No manager found with ID: " + managerId);
            }
        }
    }
    private void populateComboBoxes(){
        // Populate managerComboBox with data from the database
        List<String> departments = DepartmentDAO.findAllDepartments().stream()
                .map(department -> department.getDep_id() + " | " + department.getDep_name())
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            departments = List.of("No managers exist");
        }
        departmentComboBox.setItems(FXCollections.observableArrayList(departments));
    }

}
