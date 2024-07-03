package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;

@Entity
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int request_id;
    private String request_type;
    private String request_description;
    private String request_label;
    private String request_state;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @ManyToOne
    @JoinColumn(name = "holiday_id")
    private Holiday holiday;

    @ManyToOne
    @JoinColumn(name = "repayment_id")
    private Repayment repayment;

    // getters and setters
    public int getRequest_id() {
        return request_id;
    }

    public void setRequest_id(int request_id) {
        this.request_id = request_id;
    }

    public String getRequest_type() {
        return request_type;
    }

    public void setRequest_type(String request_type) {
        this.request_type = request_type;
    }

    public String getRequest_description() {
        return request_description;
    }

    public void setRequest_description(String request_description) {
        this.request_description = request_description;
    }

    public String getRequest_label() {
        return request_label;
    }

    public void setRequest_label(String request_label) {
        this.request_label = request_label;
    }

    public String getRequest_state() {
        return request_state;
    }

    public void setRequest_state(String request_state) {
        this.request_state = request_state;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public Holiday getHoliday() {
        return holiday;
    }

    public void setHoliday(Holiday holiday) {
        this.holiday = holiday;
    }

    public Repayment getRepayment() {
        return repayment;
    }

    public void setRepayment(Repayment repayment) {
        this.repayment = repayment;
    }
}
