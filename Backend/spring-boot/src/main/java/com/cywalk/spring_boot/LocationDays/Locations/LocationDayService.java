package com.cywalk.spring_boot.LocationDays.Locations;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Locations.LocationRepository;
import org.locationtech.jts.geom.Point;
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

    public void addLocationToDayList(Location newLogged) {
        
    }

    public void deleteLocationDay(Long id) {
        locationDayRepository.deleteById(id);
    }

    public double totalSteps(Long id) {
        LocationDay ld = locationDayRepository.getReferenceById(id);
        double distance = 0;
        List<Location> locations = ld.getLocations();
        for (int i = 1; i < locations.size(); i++) {
            distance += calculateDifference(locations.get(i).getCoordinates(), locations.get(i - 1).getCoordinates());
        }
        return distance;
    }

    private double calculateDifference(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p2.getX() - p1.getX(), 2) + Math.pow(p2.getY() - p1.getY() , 2));
    }
}

