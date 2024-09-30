package com.cywalk.spring_boot.LocationDays.Locations;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface LocationDayRepository extends JpaRepository<LocationDay, Long> {
    List<LocationDay> findByDate(LocalDate date);
    //List<Steps> findByDateAndTime(LocalDate date, LocalTime time);
}
