package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.Training;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyTrainingsController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private TableView<Training> trainingsTable;
    @FXML
    private TableColumn<Training, Integer> trainIdColumn;
    @FXML
    private TableColumn<Training, String> trainLabelColumn;
    @FXML
    private TableColumn<Training, String> trainDescriptionColumn;
    @FXML
    private TableColumn<Training, Integer> trainChaptersColumn;
    @FXML
    private TableColumn<Training, Integer> trainLengthColumn;
    @FXML
    private TableColumn<Training, String> trainImageColumn;
    @FXML
    private TableColumn<Training, Integer> trainFinishedNbColumn;

    private final String pageName = "My Trainings";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;

    public MyTrainingsController() {
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
        trainingsTable.setDisable(true);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("My Trainings");
        initializeColumns();
        loadTrainings();

        trainingsTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Training selectedTraining = trainingsTable.getSelectionModel().getSelectedItem();
                if (selectedTraining != null) {
                    saveAndOpenDocument(selectedTraining);
                }
            }
        });
    }

    private void saveAndOpenDocument(Training training) {
        try {
            File tempFile = File.createTempFile(training.getTrain_label(), ".mp4" );
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(training.getContent());
            }

            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(tempFile);
            } else {
                showAlertError("Error", "Desktop is not supported on this platform.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlertError("Error", "Failed to open the document.");
        }
    }

    private void initializeColumns() {
        trainIdColumn.setCellValueFactory(new PropertyValueFactory<>("train_id"));
        trainLabelColumn.setCellValueFactory(new PropertyValueFactory<>("train_label"));
        trainDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("train_description"));
        trainChaptersColumn.setCellValueFactory(new PropertyValueFactory<>("train_chapters"));
        trainLengthColumn.setCellValueFactory(new PropertyValueFactory<>("train_length"));
        trainImageColumn.setCellValueFactory(new PropertyValueFactory<>("train_image"));
        trainFinishedNbColumn.setCellValueFactory(new PropertyValueFactory<>("train_finished_nb"));
    }

    private void loadTrainings() {
        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        List<Training> trainingList = employee.getTrainings();
        ObservableList<Training> trainings = FXCollections.observableArrayList(trainingList);
        trainingsTable.setItems(trainings);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
