package com.cywalk.spring_boot.Friends;

import jakarta.persistence.*;
import org.springframework.cglib.core.Local;

import java.sql.Time;
import java.time.LocalTime;

@Entity
public class FriendLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private double latitude;
    private double longitude;

    private LocalTime time;

    public FriendLocation(Long id, String username, double latitude, double longitude, LocalTime time) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = time;
    }

    public FriendLocation(Long id, String username, double latitude, double longitude) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = LocalTime.now();
    }

    public FriendLocation(String username, double latitude, double longitude) {
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
        this.time = LocalTime.now();
    }

    public FriendLocation(String username, double latitude, double longitude, LocalTime time) {
        this.username = username;
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;
    }

    public FriendLocation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "FriendLocation{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", time=" + time +
                '}';
    }
}
