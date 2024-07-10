package com.innovhr.innovhrapp.models;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Teamlead {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;

//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "team_id", nullable = true)
//    private Team team;

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
//    public Team getTeam() {
//        return team;
//    }
//
//    public void setTeam(Team name) {
//        this.team = team;
//    }

}
