package com.cywalk.spring_boot.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService stepService;

    @PostMapping
    public Location createLocation(@RequestBody Location step) {
        return stepService.saveLocation(step);
    }

    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable Long id) {
        return stepService.getLocationById(id);
    }

    @GetMapping
    public List<Location> getAllLocations() {
        return stepService.getAllLocations();
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        stepService.deleteLocation(id);
    }
}

