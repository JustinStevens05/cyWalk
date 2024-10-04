package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.Type;


import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
public class Location {
    @Id
    private Long id;

    private Point coordinates;

    private LocalTime time;

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
