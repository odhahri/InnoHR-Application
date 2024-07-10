package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.models.Training;
import com.innovhr.innovhrapp.daos.TrainingDAO;
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

public class AllTrainingsController implements AccessControlled {

    @FXML
    private TableView<Training> allTrainingsTable;

    private EmployeeManageController parentController;
    private final String pageName = "All Trainings table";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public AllTrainingsController(){
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
        allTrainingsTable.setDisable(true);
    }
    @FXML
    public void initialize() {
        // Populate the table with all trainings
        ObservableList<Training> trainings = FXCollections.observableArrayList(TrainingDAO.findAllTrainings());
        allTrainingsTable.setItems(trainings);
    }

    public void setParentController(EmployeeManageController parentController) {
        this.parentController = parentController;
    }

    @FXML
    private void selectTraining() {
        Training selectedTraining = allTrainingsTable.getSelectionModel().getSelectedItem();
        if (selectedTraining != null && parentController != null) {
            parentController.addTrainingToTable(selectedTraining);
            Stage stage = (Stage) allTrainingsTable.getScene().getWindow();
            stage.close();
        }
    }
}
