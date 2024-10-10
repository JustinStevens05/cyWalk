package com.cywalk.spring_boot.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PeopleRepository extends JpaRepository<People, String> {
  Optional<People> findByUsername(String username);
    void deleteByUsername(String username);
}
