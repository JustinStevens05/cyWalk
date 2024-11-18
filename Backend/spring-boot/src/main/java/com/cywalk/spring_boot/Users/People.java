package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Friends.FriendRequest;
import com.cywalk.spring_boot.Locations.LocationDay;
import com.cywalk.spring_boot.organizations.Organization;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "username")
@Schema(description = "The people entity, which is basically user entity.")
public class People {

  @Id
  @Column(name = "username")
  @NonNull
  @Schema(description = "the username of the user")
  private String username;

  @ManyToOne
  @JoinColumn(name = "organization_id")
  @Schema(description = "The organization a user is apart of")
  private Organization organization;

  @Schema(description = "the email of the user")
  private String email;

  @Schema(description = "the requests sent out to other users")
  @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
  @NonNull
  private Set<FriendRequest> sentRequests = new HashSet<>();

  @Schema(description = "The friend requests received from other users")
  @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
  @NonNull
  private Set<FriendRequest> receivedRequests = new HashSet<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Schema(description = "All of the locations, organized as measurements in days, that a user has logged")
  private List<LocationDay> locations = new ArrayList<>();

  // Constructors, getters, and setters

  public People(@NonNull String username, String email, List<LocationDay> locations) {
    this.username = username;
    this.email = email;
    this.locations = locations != null ? locations : new ArrayList<>();
  }

  public People() {}

  // Username
  @NonNull
  public String getUsername() {
    return username;
  }

  public void setUsername(@NonNull String username) {
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
  @NonNull
  public Set<FriendRequest> getSentRequests() {
    return sentRequests;
  }

  public void setSentRequests(@NonNull Set<FriendRequest> sentRequests) {
    this.sentRequests = sentRequests;
  }

  @NonNull
  public Set<FriendRequest> getReceivedRequests() {
    return receivedRequests;
  }

  public void setReceivedRequests(@NonNull Set<FriendRequest> receivedRequests) {
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
