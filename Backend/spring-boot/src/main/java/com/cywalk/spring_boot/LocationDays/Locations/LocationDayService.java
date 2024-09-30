package com.cywalk.spring_boot.LocationDays.Locations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LocationDayService {

    @Autowired
    private LocationDayRepository locationDayRepository;

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

    public void deleteLocationDay(Long id) {
        locationDayRepository.deleteById(id);
    }

    public void totalSteps(Long id) {
        LocationDay ld = locationDayRepository.getReferenceById(id);

    }
}

