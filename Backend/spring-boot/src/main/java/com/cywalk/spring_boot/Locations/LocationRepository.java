package com.cywalk.spring_boot.Locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findByDate(LocalDate date);
    //List<Steps> findByDateAndTime(LocalDate date, LocalTime time);
}
