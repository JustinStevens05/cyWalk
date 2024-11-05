package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.People;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The total locations from the entire day.
 * Stores a list of recorded logged locations from throughout the day
 * Also store the total distance traveled by a people as calculated from the locations
 */
@Entity
public class LocationDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<LocationActivity> activities;

    /**
     * The people who took traveled took this route
     */
    @ManyToOne
    // @JoinColumn(name = "people_username")
    private People people;

    public LocationDay(LocalDate date) {
        this.date = date;
        totalDistance = 0;
        activities = new ArrayList<>();
    }

    public LocationDay() {

    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public People getUser() {
        return people;
    }

    public void setUser(People people) {
        this.people = people;
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

    public List<LocationActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<LocationActivity> locations) {
        this.activities = locations;
    }

    /**
     * Returns the last activity or the current depending on it's state
     * @return the most recent activity
     */
    public Optional<LocationActivity> getLastActivity() {
        if (!activities.isEmpty()) {
            return Optional.of(activities.get(activities.size() - 1));
        }
        return Optional.empty();
    }

}
