package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Locations.Location;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class UserModel {

    @Id
    @GeneratedValue
    private long secretKey;

    @ManyToOne
    private User user;


    public UserModel(long secretKey, User user) {
        this.secretKey = secretKey;
        this.user = user;
    }

    public UserModel(User user) {
        this.user = user;
    }

    public UserModel() {

    }


    public long getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(long secretKey) {
        this.secretKey = secretKey;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "secretKey=" + secretKey +
                ", user=" + user +
                '}';
    }
}