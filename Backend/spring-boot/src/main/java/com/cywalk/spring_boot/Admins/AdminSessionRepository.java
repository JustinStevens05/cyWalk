package com.cywalk.spring_boot.Admins;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminSessionRepository extends JpaRepository<AdminSession, String> {
}
