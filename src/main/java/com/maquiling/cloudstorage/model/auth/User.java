package com.maquiling.cloudstorage.model.auth;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "puser")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String email;
    private String password;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}