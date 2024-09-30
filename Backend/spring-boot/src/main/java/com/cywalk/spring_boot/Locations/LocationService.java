package com.cywalk.spring_boot.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {

    @Autowired
    private LocationRepository locationRepository;

    public LocationService() {

    }

    public Location saveStep(Location step) {
        return locationRepository.save(step);
    }

    public Optional<Location> getStepById(Long id) {
        return locationRepository.findById(id);
    }

    public List<Location> getAllSteps() {
        return locationRepository.findAll();
    }

    public void deleteStep(Long id) {
        locationRepository.deleteById(id);
    }
}

