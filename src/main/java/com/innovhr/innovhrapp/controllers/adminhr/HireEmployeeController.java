package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class HireEmployeeController implements AccessControlled {

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
    private ComboBox<String> departmentComboBox;
    @FXML
    private ComboBox<String> managerComboBox;
    @FXML
    private ComboBox<String> teamComboBox;


    @FXML
    private ImageView imageView;
    private byte[] imageBytes;
    private final String pageName = "Hire employee page";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    @FXML
    public void initialize() {
        populateComboBoxes();
    }
    public HireEmployeeController(){
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
    }
    @FXML
    private void importImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");
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
    private void hireEmployee() {
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
                showAlert("Error", "Department must be selected.");
                return;
            }
            int departmentId = Integer.parseInt(departmentString.split(" \\| ")[0]);






            Department department = DepartmentDAO.findDepartmentById(departmentId);

            if (department == null) {
                showAlert("Error", "Invalid Manager, Department, or Team selection.");
                return;
            }

            Employee employee = new Employee();;
            // Extract IDs from ComboBox values
            if (managerString !=null ){
                int managerId = Integer.parseInt(managerString.split(" \\| ")[0]);
                Manager manager = ManagerDAO.findManagerById(managerId);
                employee.setManager(manager);
            }
            if (teamString!=null){
                int teamId = Integer.parseInt(teamString.split(" \\| ")[0]);
                Team team = TeamDAO.findTeamById(teamId);
                employee.setTeam(team);
            }
            employee.setEmp_username(username);
            employee.setEmp_fname(firstName);
            employee.setEmp_lname(lastName);
            employee.setEmp_email(email);
            employee.setEmp_address(address);
            employee.setDepartment(department);
            employee.setEmp_phone("3453453");

            if (imageBytes != null ) {
                employee.setEmp_image(imageBytes);
            }

            EmployeeDAO.saveEmployee(employee);
            User employeeUser = new User();
            employeeUser.setUsername(employee.getEmp_username());
            employeeUser.setPassword(employee.getEmp_username()+"-InnovhrCollab");
            employeeUser.setAccessLevel(User.AccessLevel.COLLAB);
            employeeUser.setEmployee(EmployeeDAO.findEmployeeByUsername(employee.getEmp_username()));
            UserDAO.saveUser(employeeUser);
            showAlert("Success", "Employee hired successfully.");

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to hire employee. Please try again.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
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
}
