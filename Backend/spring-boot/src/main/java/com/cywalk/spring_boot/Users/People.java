package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.LocationDays.LocationDay;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class People {

  @Id
  private String username;

  private String email;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<LocationDay> locations = new ArrayList<>(); // Initialized to an empty list

  // Constructors
  public People(String username, String email, List<LocationDay> locations) {
    this.username = username;
    this.email = email;
    if (locations != null) {
      this.locations = locations;
    } else {
      this.locations = new ArrayList<>();
    }
  }

  public People() {
    // No need to initialize locations here as it's already initialized above
  }

  // Getters and Setters

  // Username
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  // Email
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  // Locations
  public List<LocationDay> getLocations() {
    return locations;
  }

  public void setLocations(List<LocationDay> locations) {
    if (locations != null) {
      this.locations = locations;
    } else {
      this.locations = new ArrayList<>();
    }
  }

  public void addLocation(LocationDay newLocation) {
    this.locations.add(newLocation);
  }

  @Override
  public String toString() {
    return "People{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", locations=" + locations +
            '}';
  }
}
