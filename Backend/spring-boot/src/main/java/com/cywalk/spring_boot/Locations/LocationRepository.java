package com.cywalk.spring_boot.Locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByTime(LocalTime time);
    //List<Steps> findByDateAndTime(LocalDate date, LocalTime time);
}
