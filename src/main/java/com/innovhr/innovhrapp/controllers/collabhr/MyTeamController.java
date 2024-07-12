package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.daos.TeamDAO;
import com.innovhr.innovhrapp.models.Team;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.stream.Collectors;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyTeamController implements AccessControlled {

    @FXML
    private Label teamLabel;
    @FXML
    private Label teamDescription;
    @FXML
    private Label teamManager;
    @FXML
    private Label teamLeader;
    @FXML
    private Label department;
    @FXML
    private ListView<String> teamMembers;

    private final String pageName = "Team Management";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;

    public MyTeamController() {
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
        teamLabel.setVisible(false);
        teamDescription.setVisible(false);
        teamManager.setVisible(false);
        teamLeader.setVisible(false);
        teamMembers.setVisible(false);
        department.setVisible(false);
    }

    @FXML
    public void initialize() {
        Team team = TeamDAO.findTeamById(SessionManager.getInstance().getLoggedInUser().getEmployee().getTeam().getTeam_id());
        if (team != null) {
            populateTeamData(team);
        } else {
            showAlert("Team not found", "No team found for the current user.");
        }
    }

    private void populateTeamData(Team team) {
        teamLabel.setText(team.getTeam_label());
        department.setText(team.getDepartment().getDep_name());
        teamDescription.setText(team.getTeam_description());
        teamManager.setText(team.getManager().getName());
        teamLeader.setText(team.getTeamlead().getEmp_fname() + " " + team.getTeamlead().getEmp_lname());
        teamMembers.setItems(FXCollections.observableArrayList(
                team.getEmployees().stream()
                        .map(emp -> emp.getEmp_fname() + " " + emp.getEmp_lname())
                        .collect(Collectors.toList())
        ));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
