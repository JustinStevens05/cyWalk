package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Friends.FriendLocationController;
import com.cywalk.spring_boot.Friends.FriendService;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Users.PeopleService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cywalk.spring_boot.leaderboard.LeaderboardService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationDayRepository locationDayRepository;

    @Autowired
    private PeopleService peopleService;

    @Autowired
    private PeopleRepository peopleRepository;

    @Autowired
    private LocationActivityRepository locationActivityRepository;

    @Autowired
    private FriendLocationController friendLocationController;

    @Autowired
    private FriendService friendService;

    @Autowired
    private LeaderboardService leaderboardService; // Inject LeaderboardService

    Logger logger = LoggerFactory.getLogger(LocationService.class);

    public LocationService() {
    }

    /**
     * Save a new location that a user has sent as raw data
     * @param key the session key of the user
     * @param location the location to add
     * @return the location that was added if it was successful. returns {@link Optional#empty()} if there was an issue
     */
    @Transactional
    public Optional<Location> saveLocation(Long key, Location location) {
       /*if (location.getTime() == null) {
            location.setTime(LocalTime.now());
        }
        */

        location = locationRepository.save(location);

        Optional<People> personRequest = peopleService.getUserFromKey(key);
        if (personRequest.isEmpty()) {
            return Optional.empty();
        }

       return Optional.of(appendLocation(personRequest.get(), location));
    }

    /**
     * starts an activity
     * @param people the user
     * @return false if there is already an activity running, true otherwise
     */
    @Transactional
    public boolean startActivity(People people) {
        if (activityIsRunning(people)) {
            return false;
        } else {
            Optional<LocationDay> ldResult = getTodaysLocation(people);
            LocationDay ld;
            if (ldResult.isEmpty()) {
                ld = new LocationDay(LocalDate.now());
                ld = locationDayRepository.save(ld);
                people.addLocation(ld);
                peopleRepository.save(people);
            } else {
                ld = ldResult.get();
            }

            List<LocationActivity> activities = ld.getActivities();
            if (activities == null) {
                activities = new ArrayList<>();
                ld.setActivities(activities);
                ld = locationDayRepository.save(ld);
            }
            LocationActivity activity = new LocationActivity();
            locationActivityRepository.save(activity);
            ld.getActivities().add(activity);
            LocationDay newLd = locationDayRepository.save(ld);
            people.getLocations().set(people.getLocations().indexOf(ld), newLd);
            peopleService.saveUser(people);

            leaderboardService.getLeaderboard();

            return true;
        }
    }

    /**
     * Fetches whether there is an activity running for the user
     * @param people the user
     * @return whether there is an active activity
     */
    @Transactional
    public boolean activityIsRunning(People people) {
        Optional<LocationDay> ld = getTodaysLocation(people);
        if (ld.isEmpty()) {
            return false;
        }

        // current session
        Optional<LocationActivity> la = ld.get().getLastActivity();
        if (la.isEmpty()) {
            logger.warn("no current activity or previous activity");
            return false;
        }
        return !la.get().isFinished();
    }

    /**
     * UNSAFE
     *
     * @param people the user
     * @return the current location activity
     */
    @Transactional
    public LocationActivity getCurrentActivity(People people) {
        return getTodaysLocation(people).get().getLastActivity().get();
    }

    /**
     * appends a location to the most recent session if there is one
     * @param people the user's username retrieved via key
     * @param location the reported location of the user
     * @return Optional.empty() is we could not add the location, otherwise we return the location saved into the database
     */
    @Transactional
    public Location appendLocation(People people, Location location) {
        if (!activityIsRunning(people)) {
            logger.warn("Activity is not running, starting activity and adding location.");
            startActivity(people);
        }

        LocationActivity la = getCurrentActivity(people);
        location = locationRepository.save(location);
        la.addLocationToActivity(location);
        locationActivityRepository.save(la);

        Location finalLocation = location;
        new Thread(
                () -> sendLocationToFriends(people, finalLocation)
        );

        // Update leaderboard after appending a new location
        leaderboardService.getLeaderboard();

        return location;
    }

    @Transactional
    public void sendLocationToFriends(People people, Location location) {
        List<People> friends = friendService.getFriends(people);
        for (People p : friends) {
            friendLocationController.sendLocation(location, people.getUsername(), p.getUsername());
        }
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    @Transactional
    public void deleteLocation(Long key, Long id) {
        locationRepository.deleteById(id);
    }

    public Optional<List<Location>> getAllLocationsOfUser(Long key) {
        Optional<People> userResult = peopleService.getUserFromKey(key);
        if (userResult.isEmpty()) {
           return Optional.empty();
        }
        else {
            ArrayList<Location> result = new ArrayList<>();
            List<LocationDay> locationDays = userResult.get().getLocations();
            for (LocationDay ld : locationDays) {
                for (LocationActivity la : ld.getActivities()) {
                    result.addAll(la.getLocations());
                }
            }
            return Optional.of(result);
        }
    }

    public LocationDay saveLocationDay(LocationDay locationDay) {
        return locationDayRepository.save(locationDay);
    }

    public Optional<LocationDay> getLocationDayById(Long id) {
        return locationDayRepository.findById(id);
    }

    public List<LocationDay> getAllLocationDays() {
        return locationDayRepository.findAll();
    }

    public void deleteLocationDay(Long id) {
        locationDayRepository.deleteById(id);
    }

    @Transactional
    public Optional<LocationDay> getTodaysLocation(Long key) {
        Optional<People> peopleResult = peopleService.getUserFromKey(key);
        if (peopleResult.isPresent()) {
            return getTodaysLocation(peopleResult.get().getUsername());
        }
        return Optional.empty();
    }

    @Transactional
    public LocationActivity getCurrentActivity(String username) {
        return getCurrentActivity(peopleService.getUserByUsername(username).get());
    }


    @Transactional
    public void endSession(String username) {
        endSession(peopleService.getUserByUsername(username).get());
    }

    @Transactional
    public void endSession(People people) {
        getCurrentActivity(people).setFinished(true);

        // Update leaderboard after ending the session
        leaderboardService.getLeaderboard();
    }

    @Transactional
    public Optional<LocationDay> getTodaysLocation(String username) {
        Optional<People> peopleResult = peopleRepository.findByUsername(username);
        if (peopleResult.isEmpty()) {
            logger.error("no user found in people table for: {}", username);
            return Optional.empty();
        }
        else {
            return getTodaysLocation(peopleResult.get());
        }
    }

    @Transactional
    public Optional<LocationDay> getTodaysLocation(People people) {
        int index = people.getLocations().size() - 1;
        if (index > -1) {
            return Optional.of(people.getLocations().get(index));
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<LocationDay> totalDistanceFromUser(Long key) {
        Optional<LocationDay> locationResult = getTodaysLocation(key);
        if (locationResult.isPresent()) {
            return totalDistance(locationResult.get().getId());
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<LocationDay> totalDistanceFromUser(String username) {
        Optional<LocationDay> locationResult = getTodaysLocation(username);
        if (locationResult.isPresent()) {
            return totalDistance(locationResult.get().getId());
        }
        return Optional.empty();
    }

    @Transactional
    public Optional<LocationDay> totalDistance(Long id) {
        LocationDay ld = locationDayRepository.getReferenceById(id);
        double distance = 0;
        List<LocationActivity> locationActivities = ld.getActivities();
        if (locationActivities != null) {
            for (LocationActivity locationActivity : locationActivities) {
                List<Location> locations = locationActivity.getLocations();
                if (locations != null) {
                    for (int i = 1; i < locations.size(); i++) {
                        distance += LocationUtils.calculateDistance(locations.get(i - 1), locations.get(i));
                    }
                }
            }
            ld.setTotalDistance(distance);
        }
        return Optional.of(locationDayRepository.save(ld));

    }
}

