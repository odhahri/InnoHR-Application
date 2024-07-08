package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeManageController {

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
        pageTitle.setText("Employee Management Page");
        empIdField.setVisible(false);
        searchButton.setOnAction(event -> searchEmployee());
        hireButton.setOnAction(event -> showHireEmployeeForm());
        saveButton.setOnAction(event -> saveEmployee());
        dischargeButton.setOnAction(event -> dischargeEmployee());
        addDocumentButton.setOnAction(event -> addDocument());
        deleteDocumentButton.setOnAction(event -> deleteDocument());
        addTrainingButton.setOnAction(event -> addTraining());
        deleteTrainingButton.setOnAction(event -> deleteTraining());

        // Populate department and team combo boxes
        populateComboBoxes();
        initializeColumns();
        populateTables();
    }

    private void setFieldsDisabled(boolean disabled) {
        usernameField.setDisable(disabled);
        fnameField.setDisable(disabled);
        lnameField.setDisable(disabled);
        emailField.setDisable(disabled);
        addressField.setDisable(disabled);
        managerComboBox.setDisable(disabled);
        departmentComboBox.setDisable(disabled);
        teamComboBox.setDisable(disabled);
        documentsTable.setDisable(disabled);
        trainingTable.setDisable(disabled);
        saveButton.setDisable(disabled);
        dischargeButton.setDisable(disabled);
        addDocumentButton.setDisable(disabled);
        deleteDocumentButton.setDisable(disabled);
        addTrainingButton.setDisable(disabled);
        deleteTrainingButton.setDisable(disabled);
    }

    private void searchEmployee() {
        String username = searchField.getText();
        Employee employee = EmployeeDAO.findEmployeeByUsername(username);
        if (employee != null) {
            populateEmployeeData(employee);
            setFieldsDisabled(false);
        } else {
            showAlert("Employee not found", "No employee found with username: " + username);
            setFieldsDisabled(true);
        }
    }

    private void showHireEmployeeForm() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/innovhr/innovhrapp/view/adminhr/hire-employee-form.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Hire Employee");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

            if (managerString == null || departmentString == null || teamString == null) {
                showAlert("Error", "Manager, Department, and Team must be selected.");
                return;
            }

            // Extract IDs from ComboBox values
            int managerId = Integer.parseInt(managerString.split(" \\| ")[0]);
            int departmentId = Integer.parseInt(departmentString.split(" \\| ")[0]);
            int teamId = Integer.parseInt(teamString.split(" \\| ")[0]);

            Manager manager = ManagerDAO.findManagerById(managerId);
            Department department = DepartmentDAO.findDepartmentById(departmentId);
            Team team = TeamDAO.findTeamById(teamId);

            if (manager == null || department == null || team == null) {
                showAlert("Error", "Invalid Manager, Department, or Team selection.");
                return;
            }

            Employee employee;
            if (empIdField.getText().isEmpty()) {
                // New employee
                employee = new Employee();
            } else {
                // Existing employee
                int employeeId = Integer.parseInt(empIdField.getText());
                employee = EmployeeDAO.findEmployeeById(employeeId);
                if (employee == null) {
                    showAlert("Error", "Employee not found.");
                    return;
                }
            }

            employee.setEmp_username(username);
            employee.setEmp_fname(firstName);
            employee.setEmp_lname(lastName);
            employee.setEmp_email(email);
            employee.setEmp_address(address);
            employee.setManager(manager);
            employee.setDepartment(department);
            employee.setTeam(team);
            if (imageBytes != null) {
                employee.setEmp_image(imageBytes);
            }

            ObservableList<Document> documents = documentsTable.getItems();
            ObservableList<Training> trainings = trainingTable.getItems();
            employee.setDocuments(documents);
            employee.setTrainings(trainings);

            EmployeeDAO.updateEmployee(employee);

            showAlert("Success", "Employee saved successfully.");
            setFieldsDisabled(true);

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save employee. Please try again.");
        }
    }


    private void dischargeEmployee() {
        if (empIdField.getText().isEmpty()) {
            showAlert("Error", "Employee ID is required.");
            return;
        } else {
            int employeeId = Integer.parseInt(empIdField.getText());
            Employee employee = EmployeeDAO.findEmployeeById(employeeId);
            if (employee != null) {
                EmployeeDAO.deleteEmployee(employee);
                showAlert("Employee discharged", "Employee discharged successfully");
                setFieldsDisabled(true);
            } else {
                showAlert("Employee not found", "No employee found with employeeId: " + employeeId);
            }
        }
    }

    private void populateEmployeeData(Employee employee) {
        empIdField.setText(String.valueOf(employee.getEmp_id()));
        usernameField.setText(employee.getEmp_username());
        fnameField.setText(employee.getEmp_fname());
        lnameField.setText(employee.getEmp_lname());
        emailField.setText(employee.getEmp_email());
        addressField.setText(employee.getEmp_address());
        managerComboBox.setValue(employee.getManager().getId() + " | " + employee.getManager().getName());
        departmentComboBox.setValue(employee.getDepartment().getDep_id() + " | " + employee.getDepartment().getDep_name());
        teamComboBox.setValue(employee.getTeam().getTeam_id() + " | " + employee.getTeam().getTeam_label());

        // Populate document table
        ObservableList<Document> documents = FXCollections.observableArrayList(employee.getDocuments());
        documentsTable.setItems(documents);

        // Populate training table
        ObservableList<Training> trainings = FXCollections.observableArrayList(employee.getTrainings());
        trainingTable.setItems(trainings);

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

    private void populateTables() {
        // Populate document table
        List<Document> documentList = DocumentDAO.findAllDocuments();
        ObservableList<Document> documents = FXCollections.observableArrayList(documentList);
        documentsTable.setItems(documents);

        // Populate training table
        List<Training> trainingList = TrainingDAO.findAllTrainings();
        ObservableList<Training> trainings = FXCollections.observableArrayList(trainingList);
        trainingTable.setItems(trainings);
    }

    private void initializeColumns() {
        // Initialize documentsTable columns
        TableColumn<Document, Long> docIdColumn = new TableColumn<>("ID");
        docIdColumn.setCellValueFactory(new PropertyValueFactory<>("doc_id"));

        TableColumn<Document, String> docLabelColumn = new TableColumn<>("Label");
        docLabelColumn.setCellValueFactory(new PropertyValueFactory<>("doc_label"));

        documentsTable.getColumns().setAll(docIdColumn, docLabelColumn);

        // Initialize trainingTable columns
        TableColumn<Training, String> trainLabelColumn = new TableColumn<>("Label");
        trainLabelColumn.setCellValueFactory(new PropertyValueFactory<>("train_label"));

        TableColumn<Training, String> trainChaptersColumn = new TableColumn<>("Chapters");
        trainChaptersColumn.setCellValueFactory(new PropertyValueFactory<>("train_chapters"));

        trainingTable.getColumns().setAll(trainLabelColumn, trainChaptersColumn);
    }

    private void addDocument() {
        // Open a new window to select documents and add them to the documentsTable
        // For simplicity, let's assume you have a method that returns a list of selected documents
        List<Document> selectedDocuments = DocumentDAO.findAllDocuments();
        documentsTable.getItems().addAll(selectedDocuments);
    }

    private void deleteDocument() {
        Document selectedDocument = documentsTable.getSelectionModel().getSelectedItem();
        if (selectedDocument != null) {
            documentsTable.getItems().remove(selectedDocument);
        } else {
            showAlert("Error", "No document selected for deletion.");
        }
    }

    private void addTraining() {
        // Open a new window to select trainings and add them to the trainingTable
        // For simplicity, let's assume you have a method that returns a list of selected trainings
        List<Training> selectedTrainings = TrainingDAO.findAllTrainings();
        trainingTable.getItems().addAll(selectedTrainings);
    }

    private void deleteTraining() {
        Training selectedTraining = trainingTable.getSelectionModel().getSelectedItem();
        if (selectedTraining != null) {
            trainingTable.getItems().remove(selectedTraining);
        } else {
            showAlert("Error", "No training selected for deletion.");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
