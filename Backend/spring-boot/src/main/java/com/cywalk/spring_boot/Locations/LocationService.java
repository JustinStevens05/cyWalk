package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.LocationDays.LocationDay;
import com.cywalk.spring_boot.LocationDays.LocationDayRepository;
import com.cywalk.spring_boot.Users.People;
import com.cywalk.spring_boot.Users.PeopleService;
import com.cywalk.spring_boot.Users.UserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private LocationDayRepository locationDayRepository;

    @Autowired
    private PeopleService personService;

    public LocationService() {

    }

    public Optional<Location> saveLocation(Long key, Location location) {
        Optional<People> personRequest = personService.getUserFromKey(key);
        if (personRequest.isEmpty()) {
            return Optional.empty();
        }

        List<LocationDay> currentLocationDays = personRequest.get().getLocations();
        if ((currentLocationDays.get(currentLocationDays.size() - 1).getDate().isEqual(LocalDate.now()))) {
            currentLocationDays.get(currentLocationDays.size() - 1).addLocation(location);
        }
        else {
           // make a new location day for the user because the most recent location day is not from today
            LocationDay ld = new LocationDay(LocalDate.now());
            ld.addLocation(location);
            LocationDay locationDay = locationDayRepository.save(ld);
            personRequest.get().addLocation(locationDay);
        }

        personService.saveUser(personRequest.get());

        return Optional.of(locationRepository.save(location));
    }

    public Optional<Location> getLocationById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public void deleteLocation(Long key, Long id) {
        locationRepository.deleteById(id);
    }
}

