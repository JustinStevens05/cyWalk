package com.cywalk.spring_boot.Admins;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminCredentialRepository extends JpaRepository<AdminCredentials, String> {
    List<AdminCredentials> findByOrganizationId(Long organizationId);
}
