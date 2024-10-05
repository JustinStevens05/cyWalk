package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.LocationDays.LocationDay;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class People {

  @Id
  private String username;

  private String email;

  @OneToMany
  private List<LocationDay> locations;

  public People(String username, String email, List<LocationDay> locations) {
    this.username = username;
    this.email = email;
    this.locations = locations;
  }

  public People() {

  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<LocationDay> getLocations() {
    return locations;
  }

  public void setLocations(List<LocationDay> steps) {
    this.locations = steps;
  }

  public void addLocation(LocationDay newLocation) {
    this.locations.add(newLocation);
  }

  @Override
  public String toString() {
    return "People{" +
            ", username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", locations=" + locations +
            '}';
  }
}



