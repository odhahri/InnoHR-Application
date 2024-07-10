package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int team_id;
    @Column(nullable = false)
    private String team_label;
    @Column(nullable = false)
    private String team_description;


    @ManyToOne
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;
    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "team")
    private List<Employee> employees;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teamlead_Empid", nullable = true)
    private Employee teamlead;



    // getters and setters
    public int getTeam_id() {
        return team_id;
    }

    public void setTeam_id(int team_id) {
        this.team_id = team_id;
    }

    public String getTeam_label() {
        return team_label;
    }

    public void setTeam_label(String team_label) {
        this.team_label = team_label;
    }

    public String getTeam_description() {
        return team_description;
    }

    public void setTeam_description(String team_description) {
        this.team_description = team_description;
    }


    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getTeamlead() {
        return teamlead;
    }

    public void setTeamlead(Employee teamlead) {
        this.teamlead = teamlead;
    }


    public Manager getManager() {
        return manager;
    }

    public void setManager(Manager manager) {
        this.manager = manager;
    }
}
