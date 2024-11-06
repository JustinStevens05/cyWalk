package com.cywalk.spring_boot.Friends;

import jakarta.persistence.*;

@Entity
public class FriendLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private double latitude;
    private double longitude;

    public FriendLocation(Long id, String username, double latitude, double longitude) {
        this.id = id;
        this.username = username;
        this.latitude = latitude;
        this.longitude = longitude;
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
}
