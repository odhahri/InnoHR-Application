package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyInfosController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private Button hireButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private TextField empIdField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField fnameField;
    @FXML
    private TextField lnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;
    @FXML
    private ComboBox<String> managerComboBox;
    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private ComboBox<String> teamComboBox;
    @FXML
    private TableView<Document> documentsTable;
    @FXML
    private TableView<Training> trainingTable;
    @FXML
    private Button dischargeButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button addDocumentButton;
    @FXML
    private Button deleteDocumentButton;
    @FXML
    private Button addTrainingButton;
    @FXML
    private Button deleteTrainingButton;
    @FXML
    private ImageView imageView;
    private byte[] imageBytes;
    private final String pageName = "Employee informations";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;

    public MyInfosController(){
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
        setFieldsDisabled(true);
    }
    @FXML
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose an Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            imageView.setImage(image);

            try {
                imageBytes = Files.readAllBytes(selectedFile.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void initialize() {
        pageTitle.setText("My personal informations");
        empIdField.setVisible(false);
        searchEmployee();
        saveButton.setOnAction(event -> saveEmployee());
        // Populate department and team combo boxes

//        setFieldsDisabled(true);
    }

    private void setFieldsDisabled(boolean disabled) {
        usernameField.setDisable(disabled);

        emailField.setDisable(disabled);
        managerComboBox.setDisable(disabled);
        departmentComboBox.setDisable(disabled);
        teamComboBox.setDisable(disabled);
    }

    private void searchEmployee() {

        Employee employee = EmployeeDAO.findEmployeeByUsername(EmployeeDAO.findEmployeeById(SessionManager.getInstance().getLoggedInUser().getEmployee().getEmp_id()).getEmp_username());
        if (employee != null) {
            populateEmployeeData(employee);
            if (AdminhrDAO.findAdminhrByEmployee(employee) != null){
                setFieldsDisabled(false);
            }else{
                setFieldsDisabled(true);
            }
        } else {
            showAlert("Employee not found", "No employee found ");
            setFieldsDisabled(true);
        }
    }


    private void saveEmployee() {
        try {
            String username = usernameField.getText();
            String firstName = fnameField.getText();
            String lastName = lnameField.getText();
            String email = emailField.getText();
            String address = addressField.getText();

            if (username.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || address.isEmpty()) {
                showAlert("Error", "All fields are required.");
                return;
            }

            String managerString = managerComboBox.getValue();
            String departmentString = departmentComboBox.getValue();
            String teamString = teamComboBox.getValue();

            if (departmentString == null ) {
                showAlert("Error", "Departmentmust be selected.");
                return;
            }

            // Extract IDs from ComboBox values

            int departmentId = Integer.parseInt(departmentString.split(" \\| ")[0]);

            Department department = DepartmentDAO.findDepartmentById(departmentId);

            if (department == null) {
                showAlert("Error", "Invalid Manager, Department, or Team selection.");
                return;
            }

            Employee employee;


                // Existing employee
                int employeeId = Integer.parseInt(empIdField.getText());
                employee = EmployeeDAO.findEmployeeById(employeeId);
                if (employee == null) {
                    showAlert("Error", "Employee not found.");
                    return;
                }


            employee.setEmp_username(username);
            employee.setEmp_fname(firstName);
            employee.setEmp_lname(lastName);
            employee.setEmp_email(email);
            employee.setEmp_address(address);
            if (imageBytes != null) {
                employee.setEmp_image(imageBytes);
            }



            EmployeeDAO.updateEmployee(employee);

            showAlert("Success", "Employee saved successfully.");
            setFieldsDisabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save employee. Please try again.");
        }
    }




    private void populateEmployeeData(Employee employee) {
        empIdField.setText(String.valueOf(employee.getEmp_id()));
        usernameField.setText(employee.getEmp_username());
        fnameField.setText(employee.getEmp_fname());
        lnameField.setText(employee.getEmp_lname());
        emailField.setText(employee.getEmp_email());
        addressField.setText(employee.getEmp_address());
        if (employee.getManager() != null){
            managerComboBox.setValue(employee.getManager().getId() + " | " + employee.getManager().getName());
        } if (employee.getDepartment() != null) {
            departmentComboBox.setValue(employee.getDepartment().getDep_id() + " | " + employee.getDepartment().getDep_name());
        }
        if (employee.getTeam() != null) {
            teamComboBox.setValue(employee.getTeam().getTeam_id() + " | " + employee.getTeam().getTeam_label());
        }

        // Populate other fields and tables
        if (employee.getEmp_image() != null) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(employee.getEmp_image());
            Image image = new Image(inputStream);
            imageView.setImage(image);
        }
    }

    private void populateComboBoxes() {
        // Populate managerComboBox with data from the database
        List<String> managers = ManagerDAO.findAllManagers().stream()
                .map(manager -> manager.getId() + " | " + manager.getName())
                .collect(Collectors.toList());

        if (managers.isEmpty()) {
            managers = List.of("No managers exist");
        }
        managerComboBox.setItems(FXCollections.observableArrayList(managers));

        // Populate departmentComboBox with data from the database
        List<String> departments = DepartmentDAO.findAllDepartments().stream()
                .map(department -> department.getDep_id() + " | " + department.getDep_name())
                .collect(Collectors.toList());

        if (departments.isEmpty()) {
            departments = List.of("No departments exist");
        }
        departmentComboBox.setItems(FXCollections.observableArrayList(departments));

        // Populate teamComboBox with data from the database
        List<String> teams = TeamDAO.findAllTeams().stream()
                .map(team -> team.getTeam_id() + " | " + team.getTeam_label())
                .collect(Collectors.toList());

        if (teams.isEmpty()) {
            teams = List.of("No teams exist");
        }
        teamComboBox.setItems(FXCollections.observableArrayList(teams));
    }








    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
