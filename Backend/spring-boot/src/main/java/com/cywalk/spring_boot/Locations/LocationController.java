package com.cywalk.spring_boot.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("{key}/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @PostMapping
    public Location createLocation(@PathVariable String key, @RequestBody Location step) {
        return locationService.saveLocation(step);
    }

    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable String key, @PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public List<Location> getAllLocations(@PathVariable String key) {
        return locationService.getAllLocations();
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable String key, @PathVariable Long id) {
        locationService.deleteLocation(id);
    }
}

