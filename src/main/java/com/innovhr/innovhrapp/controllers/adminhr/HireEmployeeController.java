package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
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

public class HireEmployeeController {

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
    private TableView<String> documentsTable;
    @FXML
    private TableView<String> trainingTable;

    @FXML
    private ImageView imageView;
    private byte[] imageBytes;

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
        if (usernameField.getText().isEmpty()) {
            showAlert("Error", "Username is required.");
            return;
        }
        if (fnameField.getText().isEmpty()) {
            showAlert("Error", "First name is required.");
            return;
        }
        if (lnameField.getText().isEmpty()) {
            showAlert("Error", "Last name is required.");
            return;
        }
        if (emailField.getText().isEmpty()) {
            showAlert("Error", "Email is required.");
            return;
        }
        if (addressField.getText().isEmpty()) {
            showAlert("Error", "Address is required.");
            return;
        }
        if (managerComboBox.getValue() == null || managerComboBox.getValue().isEmpty()) {
            showAlert("Error", "Manager is required.");
            return;
        }
        if (departmentComboBox.getValue() == null || departmentComboBox.getValue().isEmpty()) {
            showAlert("Error", "Department is required.");
            return;
        }
        if (teamComboBox.getValue() == null || teamComboBox.getValue().isEmpty()) {
            showAlert("Error", "Team is required.");
            return;
        }
        Employee employee = new Employee();
        employee.setEmp_username(usernameField.getText());
        employee.setEmp_fname(fnameField.getText());
        employee.setEmp_lname(lnameField.getText());
        employee.setEmp_email(emailField.getText());
        employee.setEmp_address(addressField.getText());
        String[] managerData = managerComboBox.getValue().split(" \\| ");
        int managerId = Integer.parseInt(managerData[0]);
        Manager selectedManager = ManagerDAO.findManagerById(managerId);
        employee.setManager(selectedManager);
        // Retrieve the selected department and team
        String[] departmentData = departmentComboBox.getValue().split(" \\| ");
        int departmentId = Integer.parseInt(departmentData[0]);
        Department selectedDepartment = DepartmentDAO.findDepartmentById(departmentId);

        String[] teamData = teamComboBox.getValue().split(" \\| ");
        int teamId = Integer.parseInt(teamData[0]);
        Team selectedTeam = TeamDAO.findTeamById(teamId);

        employee.setDepartment(selectedDepartment);
        employee.setTeam(selectedTeam);

        // Update documents
        List<Document> updatedDocuments = documentsTable.getItems().stream()
                .map(docLabel -> {
                    String[] docData = docLabel.split(" \\| ");
                    int docId = Integer.parseInt(docData[0]);
                    return DocumentDAO.findDocumentById(docId);
                })
                .collect(Collectors.toList());
        employee.setDocuments(updatedDocuments);

        // Update trainings
        List<Training> updatedTrainings = trainingTable.getItems().stream()
                .map(trainLabel -> {
                    String[] trainData = trainLabel.split(" \\| ");
                    int trainId = Integer.parseInt(trainData[0]);
                    return TrainingDAO.findTrainingById(trainId);
                })
                .collect(Collectors.toList());
        employee.setTrainings(updatedTrainings);
        // Update image if necessary
        employee.setEmp_image(imageBytes);

        EmployeeDAO.saveEmployee(employee);
        employee = EmployeeDAO.findEmployeeByUsername(usernameField.getText());
        //Creating user for this employee
        User employeeUser = new User();
        employeeUser.setPersonId(employee.getEmp_id());
        employeeUser.setUsername(employee.getEmp_username());
        employeeUser.setAccessLevel(User.AccessLevel.COLLAB);
        employeeUser.setPassword(employee.getEmp_username()+"innovhr");
        UserDAO.saveUser(employeeUser);
        showAlert("Employee Hired", "New employee hired successfully");
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
