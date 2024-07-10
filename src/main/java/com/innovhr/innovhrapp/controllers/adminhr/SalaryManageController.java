package com.innovhr.innovhrapp.controllers.adminhr;

import com.innovhr.innovhrapp.daos.EmployeeDAO;
import com.innovhr.innovhrapp.daos.SalaryDAO;
import com.innovhr.innovhrapp.models.Employee;
import com.innovhr.innovhrapp.models.Salary;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;


import javafx.scene.control.Button;


public class SalaryManageController {

    @FXML
    private TableView<Employee> employeeTable;

    @FXML
    private TableColumn<Employee, Integer> empIdColumn;

    @FXML
    private TableColumn<Employee, String> empNameColumn;

    @FXML
    private TableColumn<Employee, String> empEmailColumn;

    @FXML
    private TableView<Salary> salaryTable;

    @FXML
    private TableColumn<Salary, Integer> salaryIdColumn;

    @FXML
    private TableColumn<Salary, String> salaryStateColumn;

    @FXML
    private TableColumn<Salary, Double> salaryBaseColumn;

    @FXML
    private TableColumn<Salary, Double> salaryNetColumn;

    @FXML
    private TableColumn<Salary, Double> salaryCnssColumn;

    @FXML
    private TableColumn<Salary, Double> salaryInsuranceColumn;

    @FXML
    private TableColumn<Salary, Boolean> salaryActiveColumn;

    @FXML
    private Button addUpdateSalaryButton;

    private EmployeeDAO employeeDAO;
    private SalaryDAO salaryDAO;

    @FXML
    public void initialize() {
        employeeDAO = new EmployeeDAO();
        salaryDAO = new SalaryDAO();

        empIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        empNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        empEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        salaryIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        salaryStateColumn.setCellValueFactory(new PropertyValueFactory<>("state"));
        salaryBaseColumn.setCellValueFactory(new PropertyValueFactory<>("base"));
        salaryNetColumn.setCellValueFactory(new PropertyValueFactory<>("net"));
        salaryCnssColumn.setCellValueFactory(new PropertyValueFactory<>("cnss"));
        salaryInsuranceColumn.setCellValueFactory(new PropertyValueFactory<>("insuranceRetenu"));
        salaryActiveColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        loadEmployees();
    }

    private void loadEmployees() {
        ObservableList<Employee> employees = FXCollections.observableArrayList(employeeDAO.findAllEmployees());
        employeeTable.setItems(employees);
    }

    @FXML
    private void handleAddUpdateSalary() {
        // Add/Update salary logic
    }
}