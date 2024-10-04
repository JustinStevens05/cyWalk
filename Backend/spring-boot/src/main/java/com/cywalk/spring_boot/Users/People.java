package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Locations.Location;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class People {

  @Id
  private String username;

  private String email;

  @OneToMany
  private List<Location> locations;

  public People(String username, String email, List<Location> locations) {
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

  public List<Location> getLocations() {
    return locations;
  }

  public void setLocations(List<Location> steps) {
    this.locations = steps;
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



