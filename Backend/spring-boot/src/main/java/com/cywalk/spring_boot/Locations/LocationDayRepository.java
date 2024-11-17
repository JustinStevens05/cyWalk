package com.cywalk.spring_boot.Locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LocationDayRepository extends JpaRepository<LocationDay, Long> {
    List<LocationDay> findByDate(LocalDate date);
    Optional<LocationDay> findById(long id);
    //List<Steps> findByDateAndTime(LocalDate date, LocalTime time);
}
