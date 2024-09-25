package com.cywalk.spring_boot.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  User findById(int id);
  void deleteById(int id);
  Optional<User> findByUsername(String username);
}
