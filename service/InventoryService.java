package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.HotelDto;
import com.Backend.Projects.AirBnb.dto.HotelPriceDto;
import com.Backend.Projects.AirBnb.dto.HotelSearchDto;
import com.Backend.Projects.AirBnb.entities.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {
    void InitialiseInventory(Room room);
    void deleteAllInventory(Room room);

    Page<HotelPriceDto> searchHotels(HotelSearchDto hotelSearchDto);
}
