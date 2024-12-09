package com.cywalk.spring_boot.Admins;

import com.cywalk.spring_boot.Organizations.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {
    List<Admin> findByOrganizationId(Long organizationId);
    Optional<Admin> findByNameAndOrganization(String name, Organization organization);
}
