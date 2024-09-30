package com.cywalk.spring_boot.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/steps")
public class LocationController {

    @Autowired
    private LocationService stepService;

    @PostMapping
    public Location createStep(@RequestBody Location step) {
        return stepService.saveStep(step);
    }

    @GetMapping("/{id}")
    public Optional<Location> getStepById(@PathVariable Long id) {
        return stepService.getStepById(id);
    }

    @GetMapping
    public List<Location> getAllSteps() {
        return stepService.getAllSteps();
    }

    @DeleteMapping("/{id}")
    public void deleteStep(@PathVariable Long id) {
        stepService.deleteStep(id);
    }
}

