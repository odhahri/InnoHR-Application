package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TeamManageController {

    @FXML
    private Label pageTitle;
    @FXML
    private TextField teamIdField;
    @FXML
    private TextField teamLabelField;
    @FXML
    private TextField teamDescriptionField;

    @FXML
    private ComboBox<String> departmentComboBox;
    @FXML
    private ComboBox<String> teamLeadComboBox;
    @FXML
    private ComboBox<String> managerComboBox;
    @FXML
    private Button saveButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button clearButton;
    @FXML
    private TableView<Team> teamTable;

    @FXML
    public void initialize() {
        pageTitle.setText("Teams Management Page");
        initializeColumns();
        loadTeams();
        teamIdField.setVisible(true);
        teamIdField.setEditable(false);
        teamIdField.setDisable(true);
        saveButton.setOnAction(event -> saveTeam());
        deleteButton.setOnAction(event -> deleteTeam());
        clearButton.setOnAction(event -> clearFields());
        populateComboBoxes();
        teamTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> populateTeamData(newSelection));
    }

    private void loadTeams() {
        List<Team> teams = TeamDAO.findAllTeams();
        ObservableList<Team> teamList = FXCollections.observableArrayList(teams);
        teamTable.setItems(teamList);
    }

    private void initializeColumns() {
        TableColumn<Team, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("team_id"));

        TableColumn<Team, String> labelColumn = new TableColumn<>("Label");
        labelColumn.setCellValueFactory(new PropertyValueFactory<>("team_label"));

        TableColumn<Team, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("team_description"));


        TableColumn<Team, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDepartment().getDep_name()));

        TableColumn<Team, String> teamLeadColumn = new TableColumn<>("Team Lead");
        teamLeadColumn.setCellValueFactory(cellData -> {
            Employee teamlead = cellData.getValue().getTeamlead();
            return new SimpleStringProperty(teamlead != null ? teamlead.getEmp_username() : "None");
        });

        TableColumn<Team, String> managerColumn = new TableColumn<>("Manager");
        managerColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getManager().getName()));

        teamTable.getColumns().setAll(idColumn, labelColumn, descriptionColumn, departmentColumn, teamLeadColumn, managerColumn);
    }

    private void populateTeamData(Team team) {
        if (team != null) {
            teamIdField.setText(String.valueOf(team.getTeam_id()));
            teamLabelField.setText(team.getTeam_label());
            teamDescriptionField.setText(team.getTeam_description());
            departmentComboBox.setValue(team.getDepartment().getDep_id() + " | " + team.getDepartment().getDep_name());
            teamLeadComboBox.setValue(team.getTeamlead() != null ? team.getTeamlead().getEmp_username() : null);
            managerComboBox.setValue(team.getManager().getId() + " | " + team.getManager().getName());
            setFieldsDisabled(false);
            populateTeamLeadComboBox(team.getEmployees());
        }
    }

    private void clearFields() {
        if (!teamIdField.getText().isEmpty()) {
            teamIdField.clear();
        }
        teamLabelField.clear();
        teamDescriptionField.clear();
        departmentComboBox.getSelectionModel().clearSelection();
        teamLeadComboBox.getSelectionModel().clearSelection();
        managerComboBox.getSelectionModel().clearSelection();
        setFieldsDisabled(false);
    }

    private void saveTeam() {
        try {
            if (!validateFields()) {
                AlertUtils.showAlertError("Error", "All fields except Team Lead are required.");
                return;
            }

            Team team = createTeamFromFields();

            if (!teamIdField.getText().isEmpty()) {
                updateExistingTeam(team);
                AlertUtils.showAlertSuccess("Success", "Team updated successfully.");
            } else {
                TeamDAO.saveTeam(team);
                AlertUtils.showAlertSuccess("Success", "Team saved successfully.");
            }

            loadTeams();
            clearFields();
        } catch (NumberFormatException e) {
            AlertUtils.showAlertError("Error", "Invalid number format.");
        } catch (Exception e) {
            AlertUtils.showAlertError("Error", "Error saving team: " + e.getMessage());
        }
    }

    private boolean validateFields() {
        return !teamLabelField.getText().isEmpty() && !teamDescriptionField.getText().isEmpty()
                && departmentComboBox.getValue() != null && managerComboBox.getValue() != null;
    }

    private Team createTeamFromFields() throws Exception {
        String label = teamLabelField.getText();
        String description = teamDescriptionField.getText();
        String departmentString = departmentComboBox.getValue();
        String teamLeadString = teamLeadComboBox.getValue();
        String managerString = managerComboBox.getValue();

        int departmentId = Integer.parseInt(departmentString.split(" \\| ")[0]);
        Department department = DepartmentDAO.findDepartmentById(departmentId);
        int managerId = Integer.parseInt(managerString.split(" \\| ")[0]);
        Manager manager = ManagerDAO.findManagerById(managerId);
        Employee teamlead = EmployeeDAO.findEmployeeByUsername(teamLeadString);

        Team team = new Team();
        team.setTeam_label(label);
        team.setTeam_description(description);
        team.setDepartment(department);
        team.setManager(manager);
        team.setTeamlead(teamlead);



        return team;
    }

    private void updateExistingTeam(Team team) throws Exception {
        int id = Integer.parseInt(teamIdField.getText());
        team.setTeam_id(id);
        TeamDAO.updateTeam(team);

        if (team.getTeamlead() != null) {
            team.getTeamlead().setTeam(team);
        }
    }

    private void deleteTeam() {
        if (teamIdField.getText().isEmpty()) {
            AlertUtils.showAlertError("Error", "No team selected.");
            return;
        }

        int id = Integer.parseInt(teamIdField.getText());
        Team team = TeamDAO.findTeamById(id);
        if (team != null) {
            TeamDAO.deleteTeam(team);
            AlertUtils.showAlertSuccess("Success", "Team deleted successfully.");
            loadTeams();
            clearFields();
        } else {
            AlertUtils.showAlertError("Error", "Team not found.");
        }
    }

    private void setFieldsDisabled(boolean disabled) {
        teamLabelField.setDisable(disabled);
        teamDescriptionField.setDisable(disabled);
        departmentComboBox.setDisable(disabled);
        teamLeadComboBox.setDisable(disabled);
        managerComboBox.setDisable(disabled);
    }

    private void populateComboBoxes() {
        List<Department> departments = DepartmentDAO.findAllDepartments();
        List<Manager> managers = ManagerDAO.findAllManagers();  // Assuming you have a method to fetch all employees
        List<String> departmentItems = departments.stream()
                .map(dep -> dep.getDep_id() + " | " + dep.getDep_name())
                .collect(Collectors.toList());
        departmentComboBox.setItems(FXCollections.observableArrayList(departmentItems));

        List<String> managerItems = managers.stream()
                .map(manager -> manager.getId() + " | " + manager.getName())
                .collect(Collectors.toList());
        managerComboBox.setItems(FXCollections.observableArrayList(managerItems));
    }

    private void populateTeamLeadComboBox(List<Employee> employees) {
        List<String> teamLeadItems = employees.stream()
                .map(Employee::getEmp_username)
                .collect(Collectors.toList());
        teamLeadComboBox.setItems(FXCollections.observableArrayList(teamLeadItems));
    }
}
