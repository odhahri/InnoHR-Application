package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Training {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int train_id;
    @Column(nullable = false)
    private String train_label;
    @Column(nullable = true)
    private String train_description;
    @Column(nullable = true)
    private int train_chapters;
    @Column(nullable = true)
    private int train_length;
    @Column(nullable = true)
    private String train_image;
    @Column(nullable = true)
    private int train_finished_nb;

    @Lob
    @Column(nullable = false)
    private byte[] content;

    @ManyToMany(mappedBy = "trainings")
    private List<Employee> employees;

    // getters and setters
    public int getTrain_id() {
        return train_id;
    }

    public void setTrain_id(int train_id) {
        this.train_id = train_id;
    }

    public String getTrain_label() {
        return train_label;
    }

    public void setTrain_label(String train_label) {
        this.train_label = train_label;
    }

    public String getTrain_description() {
        return train_description;
    }

    public void setTrain_description(String train_description) {
        this.train_description = train_description;
    }

    public int getTrain_chapters() {
        return train_chapters;
    }

    public void setTrain_chapters(int train_chapters) {
        this.train_chapters = train_chapters;
    }

    public int getTrain_length() {
        return train_length;
    }

    public void setTrain_length(int train_length) {
        this.train_length = train_length;
    }

    public String getTrain_image() {
        return train_image;
    }

    public void setTrain_image(String train_image) {
        this.train_image = train_image;
    }

    public int getTrain_finished_nb() {
        return train_finished_nb;
    }

    public void setTrain_finished_nb(int train_finished_nb) {
        this.train_finished_nb = train_finished_nb;
    }


    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
