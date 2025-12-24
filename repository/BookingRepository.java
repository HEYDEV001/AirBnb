package com.Backend.Projects.AirBnb.repository;

import com.Backend.Projects.AirBnb.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}
