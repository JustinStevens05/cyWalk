package com.cywalk.spring_boot.Admins;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, String> {
    List<Admin> findByOrganizationId(Long organizationId);
}
