package com.cywalk.spring_boot.LocationDays;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationService;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleRepository;
import com.cywalk.spring_boot.Users.PeopleService;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LocationDayService {

    private static final double EARTH_RADIUS_METERS = 6371000.0; // used for getting the distance between objects

    @Autowired
    private LocationDayRepository locationDayRepository;

    @Autowired
    private LocationService locationService;

    @Autowired
    private PeopleService peopleService;


    Logger logger = LoggerFactory.getLogger(LocationDayService.class);

    @Autowired
    private PeopleRepository peopleRepository;

    public LocationDayService() {

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

    public void addLocationToDayList(People peopleAdding, Location newLogged) {
        
    }

    public void addLocationToDayList(long id, Location newLogged) {
        Optional<LocationDay> locationDayResult = locationDayRepository.findById(id);
        if (locationDayResult.isPresent()) {
            LocationDay ld = locationDayResult.get();
            ld.addLocation(newLogged);
        }
        else {
            logger.warn("Problem adding location to Day list, locationDay not findable by id.");
        }
    }

    public void addLocationToDayList(long id, long locationID) {
        Optional<Location> locationResult = locationService.getLocationById(locationID);
        if (locationResult.isPresent()) {
            addLocationToDayList(id, locationResult.get());
        }
        else {
            logger.warn("location not found when adding location to day list.");
        }
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
    public Optional<LocationDay> getTodaysLocation(String username) {
        Optional<People> peopleResult = peopleRepository.findByUsername(username);
        if (peopleResult.isEmpty()) {
            logger.error("no user found in people table for: {}", username);
            return Optional.empty();
        }
        else {
            int index = peopleResult.get().getLocations().size() - 1;
            if (index > -1) {
                return Optional.of(peopleResult.get().getLocations().get(index));
            }
            else {
                return Optional.empty();
            }
        }
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
        List<Location> locations = ld.getLocations();
        for (int i = 1; i < locations.size(); i++) {
            distance += calculateDistance(locations.get(i - 1), locations.get(i));
        }
        ld.setTotalDistance(distance);
        return Optional.of(locationDayRepository.save(ld));
    }


    /**
     * Computes the distance between two points on a sphere (the earth) using the Haverisne equation {@linkplain <a href="https://en.wikipedia.org/wiki/Haversine_formula">...</a>}
     *
     * @param location1 the first location point
     * @param location2 the second location point
     * @return the distance in meters between the points
     */
    public static double calculateDistance(Location location1, Location location2) {
        // Convert latitude and longitude from degrees to radians for caclulation
        double lat1 = Math.toRadians(location1.getLatitude());
        double lon1 = Math.toRadians(location1.getLongitude());
        double lat2 = Math.toRadians(location2.getLatitude());
        double lon2 = Math.toRadians(location2.getLongitude());

        // Differences in latitude and longitude
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Distance in meters (without considering elevation)
        double distance = EARTH_RADIUS_METERS * c;

        double elevationDiff = location2.getElevation() - location1.getElevation();
        distance = Math.sqrt(Math.pow(distance, 2) + Math.pow(elevationDiff, 2));

        return distance; // Distance in kilometers
    }

}

