package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/{key}/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private PeopleService peopleService;

    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable Long key, @PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public Optional<List<Location>> getAllLocations(@PathVariable Long key) {
        return locationService.getAllLocationsOfUser(key);
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long key, @PathVariable Long id) {
        locationService.deleteLocation(key, id);
    }

    @PostMapping("/start")
    public void startSession(@PathVariable Long key) {
        locationService.startActivity(peopleService.getUserFromKey(key).get());
    }

    @PostMapping("/log")
    public void logLocation(@PathVariable Long key, @RequestBody Location location) {
        locationService.appendLocation(peopleService.getUserFromKey(key).get(), location);
    }

    @DeleteMapping("/end")
    public void endSession(@PathVariable Long key) {
        locationService.endSession(peopleService.getUserFromKey(key).get());
    }

    @PostMapping("/day")
    public LocationDay createLocation(@PathVariable String key, @RequestBody LocationDay locationDay) {
        return locationService.saveLocationDay(locationDay);
    }

    @GetMapping("/day")
    public List<LocationDay> getAllLocationDays(@PathVariable String key) {
        return locationService.getAllLocationDays();
    }

    @GetMapping("/today")
    public Optional<LocationDay> getLocationsFromToday(@PathVariable String key) {
        return locationService.getTodaysLocation(Long.valueOf(key));
    }

    @GetMapping("/total")
    public Optional<LocationDay> getDistanceFromDay(@PathVariable Long key) {
        return locationService.totalDistanceFromUser(key);
    }
}

