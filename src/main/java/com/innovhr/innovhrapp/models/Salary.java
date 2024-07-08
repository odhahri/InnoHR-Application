package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;

@Entity
public class Salary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int salary_id;
    @Column(nullable = false)
    private String salary_state;
    @Column(nullable = false)
    private float salary_base;
    @Column(nullable = false)
    private float salary_net;
    @Column(nullable = false)
    private float salary_cnss;
    @Column(nullable = false)
    private float salary_insurance_retenu;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // getters and setters
    public int getSalary_id() {
        return salary_id;
    }

    public void setSalary_id(int salary_id) {
        this.salary_id = salary_id;
    }

    public String getSalary_state() {
        return salary_state;
    }

    public void setSalary_state(String salary_state) {
        this.salary_state = salary_state;
    }

    public float getSalary_base() {
        return salary_base;
    }

    public void setSalary_base(float salary_base) {
        this.salary_base = salary_base;
    }

    public float getSalary_net() {
        return salary_net;
    }

    public void setSalary_net(float salary_net) {
        this.salary_net = salary_net;
    }

    public float getSalary_cnss() {
        return salary_cnss;
    }

    public void setSalary_cnss(float salary_cnss) {
        this.salary_cnss = salary_cnss;
    }

    public float getSalary_insurance_retenu() {
        return salary_insurance_retenu;
    }

    public void setSalary_insurance_retenu(float salary_insurance_retenu) {
        this.salary_insurance_retenu = salary_insurance_retenu;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
