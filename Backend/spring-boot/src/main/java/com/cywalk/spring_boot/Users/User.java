package com.cywalk.spring_boot.Users;

import com.cywalk.spring_boot.Steps.Steps;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(unique = true, nullable = false)
  @get:NotBlank(message = "Username must not be blank")
  private String username;

  @Column(unique = true, nullable = false)
  @get:NotBlank(message = "email must not be blank")
  private String email;

  @OneToMany
  private List<Steps> steps;


}



