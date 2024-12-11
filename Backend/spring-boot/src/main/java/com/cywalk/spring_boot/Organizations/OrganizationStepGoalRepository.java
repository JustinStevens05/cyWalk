package com.cywalk.spring_boot.Organizations;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrganizationStepGoalRepository extends JpaRepository<OrganizationStepGoal, Long> {
    Optional<OrganizationStepGoal> findByOrganizationId(Long orgId);
}