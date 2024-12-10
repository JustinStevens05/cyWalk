package com.cywalk.spring_boot.Users;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Schema(description = "User request Entity. Stores a username password pair")
public class  UserRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Schema(description = "Request ID. If stored in the database this is it's id")
  private long id;

  @NotNull
  @Schema(description = "The user's username")
  private String username;

  @NotNull
  @Schema(description = "The user's password")
  private String password;

  public UserRequest(long id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public UserRequest(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public UserRequest() {
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserRequest{" +
            "id=" + id +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            '}';
  }
}



