package com.cywalk.spring_boot.goals;

import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class StepGoalService {

    @Autowired
    private StepGoalRepository stepGoalRepository;

    @Autowired
    private PeopleRepository peopleRepository;


    public StepGoal setStepGoal(String username, StepGoal stepGoal) {
        Optional<People> peopleOpt = peopleRepository.findById(username);
        if (peopleOpt.isPresent() && (stepGoal.getDailyGoal() >= 0) && (stepGoal.getWeeklyGoal() >= 0)) {
            if(getStepGoal(username).isPresent()) {
                deleteStepGoal(username);
            }
            stepGoal.setPeople(peopleOpt.get());
            return stepGoalRepository.save(stepGoal);
        }
        return null;
    }


    public Optional<StepGoal> getStepGoal(String username) {
        return stepGoalRepository.findByPeopleUsername(username);
    }


    public boolean deleteStepGoal(String username) {
        Optional<StepGoal> stepGoalOpt = stepGoalRepository.findByPeopleUsername(username);
        if (stepGoalOpt.isPresent()) {
            stepGoalRepository.delete(stepGoalOpt.get());
            return true;
        }
        return false;
    }
}

