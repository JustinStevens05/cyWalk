package com.cywalk.spring_boot.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findBySecretKey(long secretKey);
    void deleteBySecretKey(long secretKey);
}
