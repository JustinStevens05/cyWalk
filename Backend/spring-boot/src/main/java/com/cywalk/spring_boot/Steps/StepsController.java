package com.cywalk.spring_boot.Steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/steps")
public class StepsController {

    @Autowired
    private StepService stepService;

    @PostMapping
    public Step createStep(@RequestBody Step step) {
        return stepService.saveStep(step);
    }

    @GetMapping("/{id}")
    public Optional<Step> getStepById(@PathVariable Long id) {
        return stepService.getStepById(id);
    }

    @GetMapping
    public List<Step> getAllSteps() {
        return stepService.getAllSteps();
    }

    @DeleteMapping("/{id}")
    public void deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}

