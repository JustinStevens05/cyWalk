package com.cywalk.spring_boot.LocationDays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("{key}/location")
public class LocationDayController {

    @Autowired
    private LocationDayService locationDayService;

    @PostMapping("/day")
    public LocationDay createLocation(@PathVariable String key, @RequestBody LocationDay locationDay) {
        return locationDayService.saveLocationDay(locationDay);
    }

    @GetMapping("/{id}")
    public Optional<LocationDay> getStepById(@PathVariable String key, @PathVariable Long id) {
        return locationDayService.getLocationDayById(id);
    }

    @GetMapping("/day")
    public List<LocationDay> getAllLocationDays(@PathVariable String key) {
        return locationDayService.getAllLocationDays();
    }

    @DeleteMapping("/{id}")
    public void deleteLocationDay(@PathVariable String key, @PathVariable Long id) {
        locationDayService.deleteLocationDay(id);
    }

    @GetMapping("/total")
    public double getDistanceFromDay(@PathVariable String key) {
        return locationDayService.totalSteps(Long.valueOf(key));
    }


}

