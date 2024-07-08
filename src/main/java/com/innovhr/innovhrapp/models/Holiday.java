package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Holiday {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int holiday_id;
    @Column(nullable = false)
    private String holiday_type;
    @Column(nullable = false)
    private Date holiday_start_date;
    @Column(nullable = false)
    private Date holiday_end_date;
    @OneToMany(mappedBy = "holiday")
    private List<Request> requests;

    @ManyToMany(mappedBy = "holidays")
    private List<Employee> employees;

    // getters and setters
    public int getHoliday_id() {
        return holiday_id;
    }

    public void setHoliday_id(int holiday_id) {
        this.holiday_id = holiday_id;
    }

    public String getHoliday_type() {
        return holiday_type;
    }

    public void setHoliday_type(String holiday_type) {
        this.holiday_type = holiday_type;
    }

    public Date getHoliday_start_date() {
        return holiday_start_date;
    }

    public void setHoliday_start_date(Date holiday_start_date) {
        this.holiday_start_date = holiday_start_date;
    }

    public Date getHoliday_end_date() {
        return holiday_end_date;
    }

    public void setHoliday_end_date(Date holiday_end_date) {
        this.holiday_end_date = holiday_end_date;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}
