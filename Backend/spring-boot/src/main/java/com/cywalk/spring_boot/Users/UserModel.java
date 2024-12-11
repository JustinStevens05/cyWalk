package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

@Entity
@Schema(description = "A model that contains a secret key for a user")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "the secret key to be used in endpoints for a user")
    private long id;

    @ManyToOne
    @Schema(description = "the user who matches with the secret key")
    @NonNull
    private People people;

    public UserModel(long secretKey, @NonNull People people) {
        this.id = secretKey;
        this.people = people;
    }

    public UserModel(@NonNull People people) {
        this.people = people;
    }

    public UserModel() {

    }


    public long getId() {
        return id;
    }

    public void setId(long secretKey) {
        this.id = secretKey;
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
                "secretKey=" + id +
                ", people=" + people +
                '}';
    }
}