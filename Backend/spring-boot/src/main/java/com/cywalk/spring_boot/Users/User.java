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


}



