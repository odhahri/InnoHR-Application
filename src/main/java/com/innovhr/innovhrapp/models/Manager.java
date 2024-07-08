package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "adminhr_id" , nullable = false)
    private Adminhr adminhr;

    @OneToMany(mappedBy = "manager")
    private List<Employee> employees;

    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Adminhr getAdminhr() {
        return adminhr;
    }

    public void setAdminhr(Adminhr adminhr) {
        this.adminhr = adminhr;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
