package com.cywalk.spring_boot.steps;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;

@Entity
public class Steps {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amountOfSteps;

    @ManyToOne
    @JoinColumn(name = "people_username")
    private People user;

    // Constructors
    public Steps() {}

    public Steps(int amountOfSteps, People user) {
        this.amountOfSteps = amountOfSteps;
        this.user = user;
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public int getAmountOfSteps() {
        return amountOfSteps;
    }

    public void setAmountOfSteps(int amountOfSteps) {
        this.amountOfSteps = amountOfSteps;
    }

    public People getUser() {
        return user;
    }

    public void setUser(People user) {
        this.user = user;
    }
}
