package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int emp_id;
    @Column(nullable = false)
    private String emp_phone;
    @Column(nullable = false)
    private String emp_email;
    @Column(nullable = false)
    private String emp_address;
    @Column(nullable = false)
    @Lob
    private byte[] emp_image;
    @Column(nullable = false)
    private String emp_fname;
    @Column(nullable = false)
    private String emp_lname;
    @Column(unique = true, nullable = false)
    private String emp_username;
    @Column(nullable = false)
    private String emp_description;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @OneToMany(mappedBy = "employee")
    private List<Salary> salaries;

    @OneToMany(mappedBy = "employee")
    private List<Promotion> promotions;

    @OneToMany(mappedBy = "employee")
    private List<Request> requests;

    @ManyToMany
    @JoinTable(
            name = "employee_document",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "document_id")
    )
    private List<Document> documents;
    @ManyToMany
    @JoinTable(
            name = "employee_training",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private List<Training> trainings;

    @ManyToMany
    @JoinTable(
            name = "employee_holiday",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "holiday_id")
    )
    private List<Holiday> holidays;

    // getters and setters
    public int getEmp_id() {
        return emp_id;
    }

    public void setEmp_id(int emp_id) {
        this.emp_id = emp_id;
    }

    public String getEmp_phone() {
        return emp_phone;
    }

    public void setEmp_phone(String emp_phone) {
        this.emp_phone = emp_phone;
    }

    public String getEmp_email() {
        return emp_email;
    }

    public void setEmp_email(String emp_email) {
        this.emp_email = emp_email;
    }

    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public byte[] getEmp_image() {
        return emp_image;
    }

    public void setEmp_image(byte[] emp_image) {
        this.emp_image = emp_image;
    }

    public String getEmp_fname() {
        return emp_fname;
    }

    public void setEmp_fname(String emp_fname) {
        this.emp_fname = emp_fname;
    }

    public String getEmp_lname() {
        return emp_lname;
    }

    public void setEmp_lname(String emp_lname) {
        this.emp_lname = emp_lname;
    }
    public void setEmp_username(String emp_username) {
        this.emp_username = emp_username;
    }
    public String getEmp_username() {
        return emp_username;
    }

    public String getEmp_description() {
        return emp_description;
    }

    public void setEmp_description(String emp_description) {
        this.emp_description = emp_description;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public List<Salary> getSalaries() {
        return salaries;
    }

    public void setSalaries(List<Salary> salaries) {
        this.salaries = salaries;
    }

    public List<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(List<Promotion> promotions) {
        this.promotions = promotions;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }

    public List<Holiday> getHolidays() {
        return holidays;
    }

    public void setHolidays(List<Holiday> holidays) {
        this.holidays = holidays;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
}
