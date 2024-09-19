package com.cywalk.spring_boot;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class User {

  @Id
  @GeneratedValue(staregy = GenerationType.IDENTITY)

}



