package com.cywalk.spring_boot.Users;

import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findBySecretKey(long secretKey);
    void deleteBySecretKey(long secretKey);
    void deleteByPeople(People people);
    Optional<UserModel> findByPeople(People people);
    List<UserModel> findAllByPeople(People people);
}
