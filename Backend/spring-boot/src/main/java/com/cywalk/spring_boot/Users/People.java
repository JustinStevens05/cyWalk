package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Friends.FriendRequest;
import com.cywalk.spring_boot.LocationDays.LocationDay;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class People {

  @Id
  private String username;

  private String email;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private Set<FriendRequest> sentRequests = new HashSet<>();

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private Set<FriendRequest> receivedRequests = new HashSet<>();

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

  public Set<FriendRequest> getSentRequests() {
    return sentRequests;
  }

  public void setSentRequests(Set<FriendRequest> sentRequests) {
    this.sentRequests = sentRequests;
  }

  public Set<FriendRequest> getReceivedRequests() {
    return receivedRequests;
  }

  @Override
  public String toString() {
    return "People{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", sentRequests=" + sentRequests +
            ", receivedRequests=" + receivedRequests +
            ", locations=" + locations +
            '}';
  }

  public void setReceivedRequests(Set<FriendRequest> receivedRequests) {
    this.receivedRequests = receivedRequests;
  }

}
