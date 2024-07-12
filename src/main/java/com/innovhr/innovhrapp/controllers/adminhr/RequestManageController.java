package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.RequestDAO;
import com.innovhr.innovhrapp.models.Request;
import com.innovhr.innovhrapp.models.Holiday;
import com.innovhr.innovhrapp.models.Repayment;
import com.innovhr.innovhrapp.models.Document;
import com.innovhr.innovhrapp.models.User;
import com.innovhr.innovhrapp.utils.component.AlertUtils;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

public class RequestManageController implements AccessControlled {

    @FXML
    private Label pageTitle;
    @FXML
    private TableView<Object[]> requestsTable;
    @FXML
    private TableColumn<Object[], String> usernameColumn;
    @FXML
    private TableColumn<Object[], String> requestTypeColumn;
    @FXML
    private TableColumn<Object[], String> requestLabelColumn;
    @FXML
    private TableColumn<Object[], String> requestStateColumn;
    @FXML
    private TableColumn<Object[], String> requestDescriptionColumn;
    @FXML
    private Button approveButton;
    @FXML
    private Button refuseButton;

    private final String pageName = "Request Approval";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.ADMIN;
    private final UserNavigationHandler navigationHandler;

    public RequestManageController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }

    @Override
    public void checkAccess() {
        try {
            navigationHandler.authorizePageAccess(pageName, ControllerAccessLevel);
        } catch (UnsupportedOperationException e) {
            AlertUtils.showAlertError("Access Denied", "You do not have permission to view this page.");
            disableComponents();
        }
    }

    @FXML
    public void initialize() {
        pageTitle.setText("Request Approval");
        initializeColumns();
        loadPendingRequests();
        approveButton.setOnAction(event -> handleApproveRequest());
        refuseButton.setOnAction(event -> handleRefuseRequest());

        requestsTable.setRowFactory(tv -> {
            TableRow<Object[]> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Object[] rowData = row.getItem();
                    Request request = (Request) rowData[0];
                    showRequestDetails(request);
                }
            });
            return row;
        });
    }

    private void initializeColumns() {
        usernameColumn = new TableColumn<>("Username");
        requestDescriptionColumn.setCellValueFactory(param -> new SimpleStringProperty(((Request) param.getValue()[0]).getRequest_description()));

        usernameColumn.setCellValueFactory(param -> new SimpleStringProperty(((Request) param.getValue()[0]).getEmployee().getEmp_username()));

        requestTypeColumn.setCellValueFactory(param -> new SimpleStringProperty(((Request) param.getValue()[0]).getRequest_type()));
        requestLabelColumn.setCellValueFactory(param -> new SimpleStringProperty(((Request) param.getValue()[0]).getRequest_label()));
        requestStateColumn.setCellValueFactory(param -> new SimpleStringProperty(((Request) param.getValue()[0]).getRequest_state()));

        requestsTable.getColumns().setAll(usernameColumn, requestTypeColumn, requestLabelColumn, requestStateColumn, requestDescriptionColumn);
    }

    private void loadPendingRequests() {
        List<Object[]> pendingRequests = RequestDAO.findRequestsByStateWithEmployeeUsername("WAITING APPROBATION");
        requestsTable.setItems(FXCollections.observableArrayList(pendingRequests));
    }

    private void handleApproveRequest() {
        Object[] selectedRow = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            Request selectedRequest = (Request) selectedRow[0];
            selectedRequest.setRequest_state("APPROVED");
            RequestDAO.updateRequest(selectedRequest);
            loadPendingRequests();
            AlertUtils.showAlertSuccess("Success", "Request approved successfully.");
        } else {
            AlertUtils.showAlertError("Error", "No request selected.");
        }
    }

    private void handleRefuseRequest() {
        Object[] selectedRow = requestsTable.getSelectionModel().getSelectedItem();
        if (selectedRow != null) {
            Request selectedRequest = (Request) selectedRow[0];
            selectedRequest.setRequest_state("REFUSED");
            RequestDAO.updateRequest(selectedRequest);
            loadPendingRequests();
            AlertUtils.showAlertSuccess("Success", "Request refused successfully.");
        } else {
            AlertUtils.showAlertError("Error", "No request selected.");
        }
    }

    private void disableComponents() {
        requestsTable.setDisable(true);
        approveButton.setDisable(true);
        refuseButton.setDisable(true);
    }

    private void showRequestDetails(Request request) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        VBox dialogVbox = new VBox(20);

        Label titleLabel = new Label("Request Details");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        dialogVbox.getChildren().add(titleLabel);

        if (Objects.equals(request.getRequest_type(), "HOLIDAY")) {
            Holiday holiday = request.getHoliday();
            Label startDateLabel = new Label("Holiday Start Date: " + holiday.getHoliday_start_date());
            Label endDateLabel = new Label("Holiday End Date: " + holiday.getHoliday_end_date());
            startDateLabel.setStyle("-fx-font-size: 14px;");
            endDateLabel.setStyle("-fx-font-size: 14px;");
            dialogVbox.getChildren().addAll(startDateLabel, endDateLabel);
        } else if (Objects.equals(request.getRequest_type(), "REPAYMENT")) {
            Repayment repayment = request.getRepayment();
            Label typeLabel = new Label("Repayment Type: " + repayment.getRepayment_type());
            Label descriptionLabel = new Label("Repayment Description: " + repayment.getRepayment_description());
            Label amountLabel = new Label("Repayment Amount: " + repayment.getRepayment_amount());
            typeLabel.setStyle("-fx-font-size: 14px;");
            descriptionLabel.setStyle("-fx-font-size: 14px;");
            amountLabel.setStyle("-fx-font-size: 14px;");
            dialogVbox.getChildren().addAll(typeLabel, descriptionLabel, amountLabel);
        } else if (Objects.equals(request.getRequest_type(), "DOCUMENT")) {
            Document document = request.getDocument();
            Label labelLabel = new Label("Document Label: " + document.getDoc_label());
            Label typeLabel = new Label("Document Type: " + document.getDoc_type());
            labelLabel.setStyle("-fx-font-size: 14px;");
            typeLabel.setStyle("-fx-font-size: 14px;");
            dialogVbox.getChildren().addAll(labelLabel, typeLabel);
        }

        Scene dialogScene = new Scene(dialogVbox, 400, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }

}
