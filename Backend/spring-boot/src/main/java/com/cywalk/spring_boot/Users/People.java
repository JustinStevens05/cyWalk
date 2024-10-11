package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Friends.FriendRequest;
import com.cywalk.spring_boot.Friends.FriendService;
import com.cywalk.spring_boot.LocationDays.LocationDay;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class People {

  @Id
  private String username;

  private String email;

  @OneToMany
  private List<FriendRequest> pendingFriendRequests;

  @ManyToMany
  private List<People> friends;

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

  public List<FriendRequest> getPendingFriendRequests() {
    return pendingFriendRequests;
  }

  public void setPendingFriendRequests(List<FriendRequest> pendingFriendRequests) {
    this.pendingFriendRequests = pendingFriendRequests;
  }

  public void addLocation(LocationDay newLocation) {
    this.locations.add(newLocation);
  }

  public void addFriendRequest(FriendRequest newFr) {
    this.pendingFriendRequests.add(newFr);
  }

  public void clearFriendRequest(FriendRequest fr) {
    this.pendingFriendRequests.remove(fr);
  }

  public List<People> getFriends() {
    return friends;
  }

  public void setFriends(List<People> friends) {
    this.friends = friends;
  }

  public void addFriend(People friend) {
    friends.add(friend);
  }

  @Override
  public String toString() {
    return "People{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", pendingFriendRequests=" + pendingFriendRequests +
            ", friends=" + friends +
            ", locations=" + locations +
            '}';
  }
}
