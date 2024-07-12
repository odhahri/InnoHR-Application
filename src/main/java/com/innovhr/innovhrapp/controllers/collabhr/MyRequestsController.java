package com.innovhr.innovhrapp.controllers.collabhr;

import com.innovhr.innovhrapp.daos.*;
import com.innovhr.innovhrapp.models.*;
import com.innovhr.innovhrapp.utils.navigation.AccessControlled;
import com.innovhr.innovhrapp.utils.navigation.UserNavigationHandler;
import com.innovhr.innovhrapp.utils.usermanagment.SessionManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.innovhr.innovhrapp.utils.component.AlertUtils.showAlertError;

public class MyRequestsController implements AccessControlled {
    @FXML
    private TableColumn requestTypeColumn;
    @FXML
    private TableColumn requestLabelColumn;
    @FXML
    private TableColumn requestStateColumn;
    @FXML
    private TableColumn requestDateColumn;
    @FXML
    private Label pageTitle;
    @FXML
    private ComboBox<String> requestTypeComboBox;
    @FXML
    private Pane holidayPane;
    @FXML
    private Pane documentPane;
    @FXML
    private Pane repaymentPane;
    @FXML
    private ComboBox<String> holidayTypeComboBox;
    @FXML
    private DatePicker holidayStartDatePicker;
    @FXML
    private DatePicker holidayEndDatePicker;
    @FXML
    private ComboBox<String> documentComboBox;
    @FXML
    private TextField repaymentTypeField;
    @FXML
    private TextArea repaymentDescriptionField;
    @FXML
    private TextField repaymentAmountField;
    @FXML
    private TableView<Request> requestsTable;
    @FXML
    private Button submitRequestButton;
    @FXML
    private Button cancelButton;

    private final String pageName = "My Requests";
    private final User.AccessLevel ControllerAccessLevel = User.AccessLevel.COLLAB;
    private final UserNavigationHandler navigationHandler;

    public MyRequestsController() {
        this.navigationHandler = new UserNavigationHandler(SessionManager.getInstance());
    }
    private void initializeColumns() {
        // Initialize documentsTable columns
        TableColumn<Request, Long> requestLabelColumn = new TableColumn<>("Label");
        requestLabelColumn.setCellValueFactory(new PropertyValueFactory<>("request_label"));

        TableColumn<Request, String> requestStatusColumn = new TableColumn<>("Status");
        requestStatusColumn.setCellValueFactory(new PropertyValueFactory<>("request_state"));

        TableColumn<Request, String> requestTypesColumn = new TableColumn<>("Type");
        requestTypesColumn.setCellValueFactory(new PropertyValueFactory<>("request_type"));

        TableColumn<Request, String> requestDescColumn = new TableColumn<>("Description");
        requestDescColumn.setCellValueFactory(new PropertyValueFactory<>("request_description"));

        requestsTable.getColumns().setAll(requestLabelColumn, requestStatusColumn,requestTypesColumn,requestDescColumn);

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
        setFieldsDisabled(true);
    }

    @FXML
    public void initialize() {
        pageTitle.setText("My Requests");
        initializeRequestTypeComboBox();
        initializeHolidayTypeComboBox();
        initializeDocumentComboBox();
        loadEmployeeRequests();
        submitRequestButton.setOnAction(event -> submitRequest());
        cancelButton.setOnAction(event -> clearForm());
        setFieldsDisabled(false);
        initializeColumns();
    }

    private void initializeRequestTypeComboBox() {
        requestTypeComboBox.setItems(FXCollections.observableArrayList("HOLIDAY", "DOCUMENT", "REPAYMENT"));
        requestTypeComboBox.setOnAction(event -> updateRequestForm());
    }

    private void initializeHolidayTypeComboBox() {
        holidayTypeComboBox.setItems(FXCollections.observableArrayList("PAYED", "NON_PAYED", "FORMATION", "SICKNESS"));
    }

    private void initializeDocumentComboBox() {
        List<String> documents = DocumentDAO.findAllDocuments().stream()
                .map(Document::getDoc_label)
                .collect(Collectors.toList());
        documentComboBox.setItems(FXCollections.observableArrayList(documents));
    }

    private void loadEmployeeRequests() {
        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        List<Request> requests = RequestDAO.findRequestsByEmployee(employee.getEmp_id());
        requestsTable.setItems(FXCollections.observableArrayList(requests));
    }

    private void updateRequestForm() {
        String selectedType = requestTypeComboBox.getValue();
        holidayPane.setVisible(false);
        documentPane.setVisible(false);
        repaymentPane.setVisible(false);

        if ("HOLIDAY".equals(selectedType)) {
            holidayPane.setVisible(true);
        } else if ("DOCUMENT".equals(selectedType)) {
            documentPane.setVisible(true);
        } else if ("REPAYMENT".equals(selectedType)) {
            repaymentPane.setVisible(true);
        }
    }

    private void submitRequest() {
        String selectedType = requestTypeComboBox.getValue();

        if ("HOLIDAY".equals(selectedType)) {
            handleHolidayRequest();
        } else if ("DOCUMENT".equals(selectedType)) {
            handleDocumentRequest();
        } else if ("REPAYMENT".equals(selectedType)) {
            handleRepaymentRequest();
        }
    }

    private void handleHolidayRequest() {
        String holidayType = holidayTypeComboBox.getValue();
        LocalDate startDate = holidayStartDatePicker.getValue();
        LocalDate endDate = holidayEndDatePicker.getValue();

        if (holidayType == null || startDate == null || endDate == null) {
            showAlert("Error", "Please fill in all holiday fields.");
            return;
        }

        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        if (HolidayDAO.isHolidayOverlap(employee.getEmp_id(), startDate, endDate)) {
            showAlert("Error", "Holiday request overlaps with existing holidays.");
            return;
        }

        Holiday holiday = new Holiday();
        holiday.setHoliday_type(holidayType);
        holiday.setHoliday_start_date(java.sql.Date.valueOf(startDate));
        holiday.setHoliday_end_date(java.sql.Date.valueOf(endDate));
        holiday.setEmployee(employee);
        HolidayDAO.saveHoliday(holiday);

        Request request = new Request();
        request.setRequest_type("HOLIDAY");
        request.setRequest_label("Holiday Request");
        request.setRequest_state("WAITING APPROBATION");
        request.setEmployee(employee);
        request.setHoliday(holiday);
        RequestDAO.saveRequest(request);

        loadEmployeeRequests();
        clearForm();
    }

    private void handleDocumentRequest() {
        String documentLabel = documentComboBox.getValue();
        if (documentLabel == null) {
            showAlert("Error", "Please select a document.");
            return;
        }

        Document document = DocumentDAO.findDocumentByLabel(documentLabel);
        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        if (RequestDAO.isDocumentAlreadyRequested(employee.getEmp_id(), document.getDoc_id())) {
            showAlert("Error", "Document has already been requested.");
            return;
        }

        Request request = new Request();
        request.setRequest_type("DOCUMENT");
        request.setRequest_label("Document Request");
        request.setRequest_state("WAITING APPROBATION");
        request.setEmployee(employee);
        request.setDocument(document);
        RequestDAO.saveRequest(request);

        loadEmployeeRequests();
        clearForm();
    }

    private void handleRepaymentRequest() {
        String repaymentType = repaymentTypeField.getText();
        String repaymentDescription = repaymentDescriptionField.getText();
        String repaymentAmountText = repaymentAmountField.getText();

        if (repaymentType.isEmpty() || repaymentDescription.isEmpty() || repaymentAmountText.isEmpty()) {
            showAlert("Error", "Please fill in all repayment fields.");
            return;
        }

        float repaymentAmount;
        try {
            repaymentAmount = Float.parseFloat(repaymentAmountText);
        } catch (NumberFormatException e) {
            showAlert("Error", "Invalid repayment amount.");
            return;
        }

        Repayment repayment = new Repayment();
        repayment.setRepayment_type(repaymentType);
        repayment.setRepayment_description(repaymentDescription);
        repayment.setRepayment_amount(repaymentAmount);
        RepaymentDAO.saveRepayment(repayment);

        Employee employee = SessionManager.getInstance().getLoggedInUser().getEmployee();
        Request request = new Request();
        request.setRequest_type("REPAYMENT");
        request.setRequest_label("Repayment Request");
        request.setRequest_state("WAITING APPROBATION");
        request.setEmployee(employee);
        request.setRepayment(repayment);
        RequestDAO.saveRequest(request);

        loadEmployeeRequests();
        clearForm();
    }

    private void clearForm() {
        holidayTypeComboBox.setValue(null);
        holidayStartDatePicker.setValue(null);
        holidayEndDatePicker.setValue(null);
        documentComboBox.setValue(null);
        repaymentTypeField.clear();
        repaymentDescriptionField.clear();
        repaymentAmountField.clear();
    }

    private void setFieldsDisabled(boolean disabled) {
        requestTypeComboBox.setDisable(disabled);
        holidayTypeComboBox.setDisable(disabled);
        holidayStartDatePicker.setDisable(disabled);
        holidayEndDatePicker.setDisable(disabled);
        documentComboBox.setDisable(disabled);
        repaymentTypeField.setDisable(disabled);
        repaymentDescriptionField.setDisable(disabled);
        repaymentAmountField.setDisable(disabled);
        submitRequestButton.setDisable(disabled);
        cancelButton.setDisable(disabled);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
