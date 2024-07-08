package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Repayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int repayment_id;
    @Column(nullable = false)
    private String repayment_type;
    @Column(nullable = false)
    private String repayment_description;
    @Column(nullable = false)
    private float repayment_amount;

    @OneToMany(mappedBy = "repayment")
    private List<Request> requests;

    // getters and setters
    public int getRepayment_id() {
        return repayment_id;
    }

    public void setRepayment_id(int repayment_id) {
        this.repayment_id = repayment_id;
    }

    public String getRepayment_type() {
        return repayment_type;
    }

    public void setRepayment_type(String repayment_type) {
        this.repayment_type = repayment_type;
    }

    public String getRepayment_description() {
        return repayment_description;
    }

    public void setRepayment_description(String repayment_description) {
        this.repayment_description = repayment_description;
    }

    public float getRepayment_amount() {
        return repayment_amount;
    }

    public void setRepayment_amount(float repayment_amount) {
        this.repayment_amount = repayment_amount;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }
}
