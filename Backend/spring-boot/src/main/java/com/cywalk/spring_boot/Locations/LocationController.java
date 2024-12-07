package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.PeopleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "return a location by its id in the database")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/{id}")
    public Optional<Location> getLocationById(
            @PathVariable @Parameter(name = "key", description = "the session key for the user. Completely unused", example = "1") Long key,
            @PathVariable @Parameter(name = "id", description = "the id of the location in the database", example = "121") Long id) {
        return locationService.getLocationById(id);
    }

    @Operation(summary = "get all of the locations of a user")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping
    public Optional<List<Location>> getAllLocations(@PathVariable @Parameter(name = "key", description = "the session key of a user", example = "1") Long key) {
        return locationService.getAllLocationsOfUser(key);
    }

    @Operation(summary  = "deletes a location from the database")
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable @Parameter(name = "key", description = "the session key of the user", example = "1") Long key,
                               @PathVariable @Parameter(name = "id", description = "the id of the location in the database", example = "121") Long id) {
        locationService.deleteLocation(key, id);
    }

    @Operation(summary = "starts a location session or activity")
    @PostMapping("/start")
    public void startSession(@PathVariable @Parameter(name = "key", description = "the session key of the user", example = "1") Long key) {
        locationService.startActivity(peopleService.getUserFromKey(key).get());
    }

    @Operation(summary =  "log a location onto the current session. DOES NOT WORK IF THERE IS NO RUNNING SESSION")
    @PostMapping("/log")
    public void logLocation(@PathVariable @Parameter(name = "key", description = "the session key of the user", example = "1") Long key,
                            @RequestBody @Parameter(name = "location", description = "the location to add") Location location) {
        locationService.appendLocation(peopleService.getUserFromKey(key).get(), location);
    }

    @Operation(summary = "ends a location session or activity")
    @DeleteMapping("/end")
    public void endSession(@PathVariable @Parameter(name = "key", description = "the user session key", example = "1") Long key) {
        locationService.endSession(peopleService.getUserFromKey(key).get());
    }

    @Operation(summary = "creates a new location day. Might be useful for loading data")
    @ApiResponse(useReturnTypeSchema = true)
    @PostMapping("/day")
    public LocationDay createLocation(
            @PathVariable @Parameter(name = "key", description = "unused") Long key,
            @RequestBody @Parameter(name = "locationDay", description = "the location day to load into the database") LocationDay locationDay)
    {
        return locationService.saveLocationDay(locationDay);
    }

    @Operation(summary = "Get all of the location days in the database", description = "reminder that a location day is a list of location activities in that day. a location activity is a list of raw locations")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/day")
    public List<LocationDay> getAllLocationDays(@PathVariable @Parameter(name = "key", description = "unused") Long key) {
        return locationService.getAllLocationDays();
    }

    @Operation(summary = "get today's location")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/today")
    public Optional<LocationDay> getLocationsFromToday(@PathVariable @Parameter(name = "key", description = "the user's session key") String key) {
        return locationService.getTodaysLocation(Long.valueOf(key));
    }

    @Operation(summary = "get the LocationDay from today")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/total")
    public Optional<LocationDay> getDistanceFromDay(@PathVariable @Parameter(name = "key", description = "the user's session key") Long key) {
        return locationService.totalDistanceFromUser(key);
    }


    @Operation(summary = "get the LocationDay from today")
    @ApiResponse(useReturnTypeSchema = true)
    @GetMapping("/user/{username}/total")
    public Optional<LocationDay> getDistanceFromDayUsername(@PathVariable @Parameter(name = "key", description = "ignore for this just give a 0", example = "0") Long key,
                                                            @PathVariable @Parameter(name = "username", description = "the user's username", example = "ckugel") String username) {
        return locationService.totalDistanceFromUser(username);
    }

    @GetMapping("/week/total")
    @Operation(summary = "Weekly distance", description = "gets the total distance traveled by a user over the course of the past week")
    @ApiResponse(useReturnTypeSchema = true)
    public Optional<Double> getTotalDistancePastWeek(@PathVariable Long key) {
        return locationService.totalDistanceFromUserWeek(key);
    }


    @GetMapping("/user/{username}/week/total")
    @Operation(summary = "Weekly distance", description = "gets the total distance traveled by a user over the course of the past week")
    @ApiResponse(useReturnTypeSchema = true)
    public Optional<Double> getTotalDistancePastWeek(@PathVariable @Parameter(name = "key", description = "ignore, fill 0", example = "0") Long key,
                                                     @PathVariable @Parameter(name = "username", description = "the user's id", example = "1") String username) {
        return locationService.totalDistanceFromUserWeek(username);
    }


}

