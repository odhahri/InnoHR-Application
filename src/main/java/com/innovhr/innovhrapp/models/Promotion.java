package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.Date;

@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int promo_id;
    private String promo_type;
    private Date promo_date;
    private float promo_amount;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    // getters and setters
    public int getPromo_id() {
        return promo_id;
    }

    public void setPromo_id(int promo_id) {
        this.promo_id = promo_id;
    }

    public String getPromo_type() {
        return promo_type;
    }

    public void setPromo_type(String promo_type) {
        this.promo_type = promo_type;
    }

    public Date getPromo_date() {
        return promo_date;
    }

    public void setPromo_date(Date promo_date) {
        this.promo_date = promo_date;
    }

    public float getPromo_amount() {
        return promo_amount;
    }

    public void setPromo_amount(float promo_amount) {
        this.promo_amount = promo_amount;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
