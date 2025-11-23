package com.Backend.Projects.AirBnb.repository;

import com.Backend.Projects.AirBnb.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    void findHotelById(Long id);
}
