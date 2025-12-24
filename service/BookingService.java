package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.BookingDto;
import com.Backend.Projects.AirBnb.dto.BookingRequest;
import com.Backend.Projects.AirBnb.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto initializeBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList);
}
