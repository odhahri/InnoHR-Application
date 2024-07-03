package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dep_id;
    private String dep_name;
    private String dep_description;
    private String dep_email;
    private int dep_team_nb;
    private int dep_employee_nb;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees;

    @OneToMany(mappedBy = "department")
    private List<Team> teams;

    // getters and setters
    public int getDep_id() {
        return dep_id;
    }

    public void setDep_id(int dep_id) {
        this.dep_id = dep_id;
    }

    public String getDep_name() {
        return dep_name;
    }

    public void setDep_name(String dep_name) {
        this.dep_name = dep_name;
    }

    public String getDep_description() {
        return dep_description;
    }

    public void setDep_description(String dep_description) {
        this.dep_description = dep_description;
    }

    public String getDep_email() {
        return dep_email;
    }

    public void setDep_email(String dep_email) {
        this.dep_email = dep_email;
    }

    public int getDep_team_nb() {
        return dep_team_nb;
    }

    public void setDep_team_nb(int dep_team_nb) {
        this.dep_team_nb = dep_team_nb;
    }

    public int getDep_employee_nb() {
        return dep_employee_nb;
    }

    public void setDep_employee_nb(int dep_employee_nb) {
        this.dep_employee_nb = dep_employee_nb;
    }

    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }
}
