package com.cywalk.spring_boot.Goals;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
@Entity
public class StepGoal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dailyGoal;
    private int weeklyGoal;

    @OneToOne
    @JoinColumn(name = "people_username")
    private People people;


    public StepGoal() {}

    public StepGoal(int dailyGoal, int weeklyGoal, People people) {
        this.dailyGoal = dailyGoal;
        this.weeklyGoal = weeklyGoal;
        this.people = people;
    }



    public Long getId() {
        return id;
    }

    public int getDailyGoal() {
        return dailyGoal;
    }

    public void setDailyGoal(int dailyGoal) {
        this.dailyGoal = dailyGoal;
    }

    public int getWeeklyGoal() {
        return weeklyGoal;
    }

    public void setWeeklyGoal(int weeklyGoal) {
        this.weeklyGoal = weeklyGoal;
    }

    public People getPeople() {
        return people;
    }

    public void setPeople(People people) {
        this.people = people;
    }
}
