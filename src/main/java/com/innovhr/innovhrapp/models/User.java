package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    @Size(min = 3, max = 50)
    private String username;

    @Column(nullable = false)
    @Size(min = 6, max = 100)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccessLevel accessLevel;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = hashPassword(password);
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    private String hashPassword(String password) {
        // Implement hashing logic here
        // Example using a simple hashing (not recommended for production)
        return Integer.toHexString(password.hashCode());
    }

    public enum AccessLevel {
        ADMIN, MANAGER, TEAMLEAD, COLLAB;
    }
}
