package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Achievements.Achievement;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.cywalk.spring_boot.Friends.FriendRequest;
import com.cywalk.spring_boot.Locations.LocationDay;
import com.cywalk.spring_boot.Organizations.Organization;
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
  @JsonBackReference
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

  @ManyToMany
  @JoinTable(name = "user_achievements",
          joinColumns = @JoinColumn(name = "username", referencedColumnName = "username"),
          inverseJoinColumns = @JoinColumn(name = "achievement_id"))
  @JsonBackReference
  private Set<Achievement> achievements = new HashSet<>();



  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @Schema(description = "All of the locations, organized as measurements in days, that a user has logged")
  private List<LocationDay> locations = new ArrayList<>();

  @OneToOne
  @Schema(description = "The image object for the profile picture")
  private Image image;

  @Enumerated(EnumType.STRING)
  private League league;

  @Schema(description = "the user's bio on their profile")
  private String bio;

  // Constructors, getters, and setters

  public People(@NonNull String username, String email, List<LocationDay> locations) {
    this.username = username;
    this.email = email;
    this.locations = locations != null ? locations : new ArrayList<>();
  }

  public People(String username, Organization organization, String email, Set<FriendRequest> sentRequests, Set<FriendRequest> receivedRequests, List<LocationDay> locations, League league) {
    this.username = username;
    this.organization = organization;
    this.email = email;
    this.sentRequests = sentRequests;
    this.receivedRequests = receivedRequests;
    this.locations = locations;
    this.league = league;
  }


  public People(@NonNull String username, Organization organization, String email, @NonNull Set<FriendRequest> sentRequests, @NonNull Set<FriendRequest> receivedRequests, List<LocationDay> locations, Image image, League league) {
    this.username = username;
    this.organization = organization;
    this.email = email;
    this.sentRequests = sentRequests;
    this.receivedRequests = receivedRequests;
    this.locations = locations;
    this.image = image;
    this.league = league;
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

  public Set<Achievement> getAchievements() {
    return achievements;
  }

  public void addAchievement(Achievement achievement) {
    this.achievements.add(achievement);
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

  public String getBio() {
    return bio;
  }

  public void setBio(String bio) {
    this.bio = bio;
  }

  public League getLeague() {
    return league;
  }

  public void setLeague(League league) {
    this.league = league;
  }

  public Image getImage() {
    return image;
  }

  public void setImage(Image image) {
    this.image = image;
  }

  @Override
  public String toString() {
    return "People{" +
            "username='" + username + '\'' +
            ", organization=" + organization +
            ", email='" + email + '\'' +
            ", sentRequests=" + sentRequests +
            ", receivedRequests=" + receivedRequests +
            ", locations=" + locations +
            ", image=" + image +
            ", league=" + league +
            ", bio='" + bio + '\'' +
            '}';
  }
}
