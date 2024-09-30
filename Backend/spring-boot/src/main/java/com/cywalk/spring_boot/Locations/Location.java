package com.cywalk.spring_boot.Locations;

import com.cywalk.spring_boot.Users.User;
import jakarta.persistence.*;
import org.locationtech.jts.geom.Point;
import org.hibernate.annotations.Type;


import java.time.LocalDateTime;

@Entity
public class Location {
    @Id
    private Long id;

    private Point coordinates;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Point getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Point coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
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
}
