package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.HotelDto;

public interface HotelService {
    HotelDto createHotel(HotelDto hotelDto);
    HotelDto getHotelById(Long id);
    HotelDto updateHotelById(Long id, HotelDto hotelDto);
    void deleteHotelById(Long id);
    void activateHotel(Long id);
}
