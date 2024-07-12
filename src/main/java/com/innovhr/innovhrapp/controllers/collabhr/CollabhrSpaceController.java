package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.CustomCellFactory;
import com.innovhr.innovhrapp.utils.component.FXMLViewLoader;
import com.innovhr.innovhrapp.utils.constants.ViewPresets;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.io.IOException;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class CollabhrSpaceController implements AccessControlled {
    private final String pageName = "CollabhrSpace";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;

    @FXML
    private HBox CollabSpaceHBOX;
    @FXML
    private ListView<String> menuListView;

    @FXML
    private AnchorPane mainContainer;

    private final UserNavigationHandler navigationHandler;

    public CollabhrSpaceController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }

    @FXML
    public void initialize() {
        checkAccess();
        // Populate menu items
        menuListView.getItems().addAll("My Infos", "Personal Documents", "Documents", "Requests", "Team", "Team Plan", "Trainings");
        if (SessionManager.getInstance().getLoggedInUser().getAccessLevel().equals(User.AccessLevel.ADMIN) || SessionManager.getInstance().getLoggedInUser().getAccessLevel().equals(User.AccessLevel.MANAGER)) {
            menuListView.getItems().add("Spaces");
        }
        menuListView.getItems().add("Disconnect");
        menuListView.setCellFactory(listView -> new CustomCellFactory());
        menuListView.getSelectionModel().select("My Infos");
        String fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_infos_path;
        loadView(fxmlFile);
    }

    @FXML
    public void handleMenuClick(MouseEvent event) throws IOException {
        String selectedItem = menuListView.getSelectionModel().getSelectedItem();
        String fxmlFile = "";

        switch (selectedItem) {
            case "My Infos":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_infos_path;
                break;
            case "Personal Documents":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_perso_docs_path;
                break;
            case "Requests":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_requests_path;
                break;
            case "Documents":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_docs_path;
                break;
            case "Team":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_team_path;
                break;
            case "Team Plan":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_teamplan_path;
                break;
            case "Trainings":
                fxmlFile = ViewPresets.CollabFxmlViews.fxml_collab_trains_path;
                break;
            case "Spaces":
                if (SessionManager.getInstance().getLoggedInUser().getAccessLevel().equals(User.AccessLevel.ADMIN)){
                    FXMLViewLoader.loadScene("Continue as", ViewPresets.SharedFXMLViews.fxml_continue_as_path );
                    FXMLViewLoader.closeScene(CollabSpaceHBOX);
                }
                if (SessionManager.getInstance().getLoggedInUser().getAccessLevel().equals(User.AccessLevel.MANAGER)){
                    FXMLViewLoader.loadScene("Continue as", ViewPresets.SharedFXMLViews.fxml_continue_as_path );
                    FXMLViewLoader.closeScene(CollabSpaceHBOX);
                }
                break;
            case "Disconnect":
                SessionManager.getInstance().logout();
                FXMLViewLoader.closeScene(CollabSpaceHBOX);
                break;
            default:
                // Handle default case if needed
                break;
        }

        if (!fxmlFile.isEmpty()) {
            loadView(fxmlFile);
        }
    }

    private void loadView(String fxmlFile) {
        try {
            Object controller = FXMLViewLoader.loadSceneContainer(mainContainer, fxmlFile);
            // If the loaded controller implements AccessControlled, perform access check
            if (controller instanceof AccessControlled) {
                ((AccessControlled) controller).checkAccess();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        menuListView.setDisable(true);
        mainContainer.setDisable(true);
    }
}
