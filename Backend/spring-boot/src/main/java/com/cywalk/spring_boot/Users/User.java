package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Steps.Steps;
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
  private List<Steps> steps;

  public User(long id, String username, String email, List<Steps> steps) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.steps = steps;
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

  public List<Steps> getSteps() {
    return steps;
  }

  public void setSteps(List<Steps> steps) {
    this.steps = steps;
  }
}



