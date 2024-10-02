package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Locations.Location;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private String username;
  private String email;

  @OneToMany
  private List<Location> locations;

  public User(long id, String username, String email, List<Location> locations) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.locations = locations;
  }

  public User() {

  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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


}



