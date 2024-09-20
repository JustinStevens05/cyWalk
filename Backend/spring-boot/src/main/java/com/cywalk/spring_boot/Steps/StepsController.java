package com.cywalk.spring_boot.Steps;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/steps")
public class StepsController {

    @Autowired
    private StepsService stepService;

    @PostMapping
    public Steps createStep(@RequestBody Steps step) {
        return stepService.saveStep(step);
    }

    @GetMapping("/{id}")
    public Optional<Steps> getStepById(@PathVariable Long id) {
        return stepService.getStepById(id);
    }

    @GetMapping
    public List<Steps> getAllSteps() {
        return stepService.getAllSteps();
    }

    @DeleteMapping("/{id}")
    public void deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}

