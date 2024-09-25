package com.cywalk.spring_boot.Steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StepsService {

    @Autowired
    private StepRepository stepRepository;

    public StepsService() {

    }

    public Steps saveStep(Steps step) {
        return stepRepository.save(step);
    }

    public Optional<Steps> getStepById(Long id) {
        return stepRepository.findById(id);
    }

    public List<Steps> getAllSteps() {
        return stepRepository.findAll();
    }

    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }
}

