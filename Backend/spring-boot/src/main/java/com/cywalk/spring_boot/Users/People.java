package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Friends.FriendRequest;
import com.cywalk.spring_boot.Locations.LocationDay;
import com.cywalk.spring_boot.Organizations.Organization;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
public class People {

  @Id
  @Column(name = "username")
  private String username;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  private Organization organization;

  private String email;

  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  private Set<FriendRequest> sentRequests = new HashSet<>();

  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  private Set<FriendRequest> receivedRequests = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  private List<LocationDay> locations = new ArrayList<>();

  // Constructors, getters, and setters

  public People(String username, String email, List<LocationDay> locations) {
    this.username = username;
    this.email = email;
    this.locations = locations != null ? locations : new ArrayList<>();
  }

  public People() {}

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

  // Organization
  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  // Locations
  public List<LocationDay> getLocations() {
    return locations;
  }

  public void setLocations(List<LocationDay> locations) {
    this.locations = locations != null ? locations : new ArrayList<>();
  }

  public void addLocation(LocationDay newLocation) {
    this.locations.add(newLocation);
  }

  // Friend requests
  public Set<FriendRequest> getSentRequests() {
    return sentRequests;
  }

  public void setSentRequests(Set<FriendRequest> sentRequests) {
    this.sentRequests = sentRequests;
  }

  public Set<FriendRequest> getReceivedRequests() {
    return receivedRequests;
  }

  public void setReceivedRequests(Set<FriendRequest> receivedRequests) {
    this.receivedRequests = receivedRequests;
  }

  @Override
  public String toString() {
    return "People{" +
            "username='" + username + '\'' +
            ", email='" + email + '\'' +
            ", organization=" + organization +
            ", sentRequests=" + sentRequests +
            ", receivedRequests=" + receivedRequests +
            ", locations=" + locations +
            '}';
  }
}
