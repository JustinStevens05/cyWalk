package com.cywalk.spring_boot.Admins;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminCredentialRepository extends JpaRepository<AdminCredentials, String> {
    Optional<AdminCredentials> findAdminCredentialsByAdmin(Admin admin);
}
