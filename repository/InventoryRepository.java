package com.Backend.Projects.AirBnb.repository;

import com.Backend.Projects.AirBnb.entities.Hotel;
import com.Backend.Projects.AirBnb.entities.Inventory;
import com.Backend.Projects.AirBnb.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    void deleteByRoom(Room room);
   boolean findByHotelAndRoomAndDate(Hotel hotel, Room room, LocalDateTime date);
}
