package com.cywalk.spring_boot.LocationDays;

import com.cywalk.spring_boot.Locations.Location;
import com.cywalk.spring_boot.Users.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The total locations from the entire day.
 * Stores a list of recorded logged locations from throughout the day
 * Also store the total distance traveled by a user as calculated from the locations
 */
@Entity
public class LocationDay {
    @Id
    private Long id;

    /**
     * The date the data was recorded on
     */
    private LocalDate date;

    /**
     * The total distance that was traveled in the day
     */
    private double totalDistance;

    /**
     * A list of keys to the location table in the database
     */
    @OneToMany
    private List<Location> locations;

    /**
     * The user who took traveled took this route
     */
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public void addLocation(Location newLocation) {
        this.locations.add(newLocation);
    }
}
