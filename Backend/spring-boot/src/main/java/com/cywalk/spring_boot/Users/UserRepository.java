package com.cywalk.spring_boot;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  User findById(int id);
  void deleteById(int id); 
}
