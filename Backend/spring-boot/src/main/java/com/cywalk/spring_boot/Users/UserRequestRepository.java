package com.cywalk.spring_boot.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRequestRepository extends JpaRepository<UserRequest, Long> {
  UserRequest findById(int id);
  void deleteById(int id);
  Optional<UserRequest> findByUsername(String username);
}
