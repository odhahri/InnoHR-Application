package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.TrainingDAO;
import com.innovhr.innovhrapp.models.Training;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class TrainingManageController implements AccessControlled {

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
    @FXML
    private Button addTrainingButton;
    @FXML
    private Button deleteTrainingButton;

    private final String pageName = "Training management";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public TrainingManageController() {
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
        addTrainingButton.setDisable(true);
        deleteTrainingButton.setDisable(true);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("Training Management Page");
        initializeColumns();
        loadTrainings();

        addTrainingButton.setOnAction(event -> addTraining());
        deleteTrainingButton.setOnAction(event -> deleteTraining());
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
        List<Training> trainingList = TrainingDAO.findAllTrainings();
        ObservableList<Training> trainings = FXCollections.observableArrayList(trainingList);
        trainingsTable.setItems(trainings);
    }

    private void addTraining() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose a Training");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF Files", "*.mp4"),
                new FileChooser.ExtensionFilter("PDF Files", "*.m4v")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                byte[] fileContent = Files.readAllBytes(selectedFile.toPath());
                String fileType = getFileExtension(selectedFile);

                Training training = new Training();
                training.setTrain_label(selectedFile.getName());
                training.setContent(fileContent);

                TrainingDAO.saveTraining(training);
                trainingsTable.getItems().add(training);

                showAlert("Document Added", "Document added successfully!");
            } catch (IOException e) {
                e.printStackTrace();
                showAlertError("Error", "Failed to add document. Please try again.");
            }
        }}
    private String getFileExtension(File file) {
        String fileName = file.getName();
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        } else {
            return "";
        }
    }

    private void deleteTraining() {
        Training selectedTraining = trainingsTable.getSelectionModel().getSelectedItem();
        if (selectedTraining != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Training");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this training?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                TrainingDAO.deleteTraining(selectedTraining);
                trainingsTable.getItems().remove(selectedTraining);

                showAlert("Training Deleted", "Training deleted successfully!");
            }
        } else {
            showAlertError("Error", "No training selected.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
