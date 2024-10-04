package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.User;
import com.cywalk.spring_boot.Users.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("{key}/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;

    @PostMapping
    public Optional<Location> createLocation(@PathVariable Long key, @RequestBody Location step) {
        Optional<User> user = userService.getUserFromKey(key);
        if (user.isPresent()) {
            return locationService.saveLocation(key, step);
        }
        else {
            return Optional.empty();
        }
    }

    @GetMapping("/{id}")
    public Optional<Location> getLocationById(@PathVariable Long key, @PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping
    public List<Location> getAllLocations(@PathVariable Long key) {
        return locationService.getAllLocations();
    }

    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long key, @PathVariable Long id) {
        locationService.deleteLocation(key, id);
    }
}

