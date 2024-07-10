package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int doc_id;
    @Column(nullable = false)
    private String doc_label;
    @Column(nullable = false)
    private String doc_type;

    @Lob
    @Column(nullable = false)
    private byte[] content;
    @ManyToMany(mappedBy = "documents")
    private List<Employee> employees;

    @OneToMany(mappedBy = "document")
    private List<Request> requests;

    // getters and setters
    public int getDoc_id() {
        return doc_id;
    }

    public void setDoc_id(int doc_id) {
        this.doc_id = doc_id;
    }

    public String getDoc_label() {
        return doc_label;
    }

    public void setDoc_label(String doc_label) {
        this.doc_label = doc_label;
    }

    public String getDoc_type() {
        return doc_type;
    }

    public void setDoc_type(String doc_type) {
        this.doc_type = doc_type;
    }

    public List<Request> getRequests() {
        return requests;
    }

    public void setRequests(List<Request> requests) {
        this.requests = requests;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
