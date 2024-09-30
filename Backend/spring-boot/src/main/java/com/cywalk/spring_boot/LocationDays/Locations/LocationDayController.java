package com.cywalk.spring_boot.LocationDays.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/locationDays")
public class LocationDayController {

    @Autowired
    private LocationDayService stepService;

    @PostMapping
    public LocationDay createLocation(@RequestBody LocationDay locationDay) {
        return stepService.saveLocationDay(locationDay);
    }

    @GetMapping("/{id}")
    public Optional<LocationDay> getStepById(@PathVariable Long id) {
        return stepService.getLocationDayById(id);
    }

    @GetMapping
    public List<LocationDay> getAllLocationDays() {
        return stepService.getAllLocationDays();
    }

    @DeleteMapping("/{id}")
    public void deleteLocationDay(@PathVariable Long id) {
        stepService.deleteLocationDay(id);
    }
}

