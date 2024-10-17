package com.cywalk.spring_boot.LocationDays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/{key}/location")
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

    @GetMapping("/today")
    public Optional<LocationDay> getLocationsFromToday(@PathVariable String key) {
        return locationDayService.getTodaysLocation(Long.valueOf(key));
    }

    @DeleteMapping("/{id}")
    public void deleteLocationDay(@PathVariable String key, @PathVariable Long id) {
        locationDayService.deleteLocationDay(id);
    }

    @GetMapping("/total")
    public Optional<LocationDay> getDistanceFromDay(@PathVariable String key) {
        return locationDayService.totalDistanceFromUser(Long.valueOf(key));
    }


}

