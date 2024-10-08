package com.cywalk.spring_boot.Users;

import jakarta.persistence.*;

@Entity
public class UserModel {

    @Id
    @GeneratedValue
    private long secretKey;

    @ManyToOne
    private People people;


    public UserModel(long secretKey, People people) {
        this.secretKey = secretKey;
        this.people = people;
    }

    public UserModel(People people) {
        this.people = people;
    }

    public UserModel() {

    }


    public long getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(long secretKey) {
        this.secretKey = secretKey;
    }

    public People getUser() {
        return people;
    }

    public void setUser(People people) {
        this.people = people;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "secretKey=" + secretKey +
                ", people=" + people +
                '}';
    }
}