package com.cywalk.spring_boot.Goals;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StepGoalRepository extends JpaRepository<StepGoal, Long> {
    Optional<StepGoal> findByPeopleUsername(String username);
}
