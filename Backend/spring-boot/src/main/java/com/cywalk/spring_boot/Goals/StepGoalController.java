package com.cywalk.spring_boot.Goals;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/goals")
public class StepGoalController {

    @Autowired
    private StepGoalService stepGoalService;


    @PostMapping("/{username}")
    public ResponseEntity<StepGoal> setStepGoal(@PathVariable String username, @RequestBody StepGoal stepGoal) {
        StepGoal savedStepGoal = stepGoalService.setStepGoal(username, stepGoal);
        if (savedStepGoal != null) {
            return ResponseEntity.ok(savedStepGoal);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/{username}")
    public ResponseEntity<StepGoal> getStepGoal(@PathVariable String username) {
        Optional<StepGoal> stepGoalOpt = stepGoalService.getStepGoal(username);
        return stepGoalOpt.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteStepGoal(@PathVariable String username) {
        boolean deleted = stepGoalService.deleteStepGoal(username);
        if (deleted) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
