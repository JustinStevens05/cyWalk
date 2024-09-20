package com.cywalk.spring_boot.Steps;

import com.cywalk.spring_boot.Steps.Steps;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Steps {


    @Id
    private Long id;

    private int amountOfSteps;

    


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
