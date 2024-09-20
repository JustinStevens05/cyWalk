package com.cywalk.spring_boot.Steps;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface StepRepository extends JpaRepository<Steps, Long> {
    List<Steps> findByDate(LocalDate date);
    List<Steps> findByDateAndTime(LocalDate date, LocalTime time);
}
