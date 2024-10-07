package com.cywalk.spring_boot.Locations;

import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;


import java.time.LocalTime;

@Entity
public class Location {
    @Id
    private Long id;

    private Point coordinates;

    private LocalTime time;

    public Location() {

    }

    public Location(Point coordinates) {
        this.coordinates = coordinates;
        time = LocalTime.now();
    }

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
