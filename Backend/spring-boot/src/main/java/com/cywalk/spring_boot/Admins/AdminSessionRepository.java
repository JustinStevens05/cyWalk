package com.cywalk.spring_boot.Admins;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminSessionRepository extends JpaRepository<AdminSession, Long> {
    Optional<AdminSession> findByAdmin(Admin admin);
}
