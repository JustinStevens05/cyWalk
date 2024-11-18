package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Schema(description = "A model that contains a secret key for a user")
public class UserModel {

    @Id
    @GeneratedValue
    @Schema(description = "the secret key to be used in endpoints for a user")
    private long secretKey;

    @ManyToOne
    @Schema(description = "the user who matches with the secret key")
    @NonNull
    private People people;

    public UserModel(long secretKey, @NonNull People people) {
        this.secretKey = secretKey;
        this.people = people;
    }

    public UserModel(@NonNull People people) {
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