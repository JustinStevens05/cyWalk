package com.cywalk.spring_boot.Admins;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class AdminModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;


    private String username;

    private String password;

    public AdminModel() {}

    public AdminModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
