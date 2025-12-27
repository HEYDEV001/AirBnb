package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.BookingDto;
import com.Backend.Projects.AirBnb.dto.BookingRequest;
import com.Backend.Projects.AirBnb.dto.GuestDto;
import com.Backend.Projects.AirBnb.entities.*;
import com.Backend.Projects.AirBnb.entities.enums.BookingStatus;
import com.Backend.Projects.AirBnb.exceptions.ResourceNotFoundException;
import com.Backend.Projects.AirBnb.exceptions.UnAuthorizedException;
import com.Backend.Projects.AirBnb.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;
    private final BookingRepository bookingRepository;
    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;
    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public BookingDto initializeBooking(BookingRequest bookingRequest) {
        log.info("Initializing Booking for the Hotel with id {} from {} to {} ", bookingRequest.getHotelId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository
                .findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id : " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id : " + bookingRequest.getRoomId()));

        log.info("Getting and locking the available  Inventory");
        List<Inventory> inventoryList = inventoryRepository.getAndLockAvailableInventory(
                room.getId(),
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate(),
                bookingRequest.getRoomsCount());

        long dateCount = ChronoUnit.DAYS.between(
                bookingRequest.getCheckInDate(),
                bookingRequest.getCheckOutDate()
        ) + 1;

        if (inventoryList.size() != dateCount) {
            throw new IllegalArgumentException("Room are not available Anymore ");
        }

        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }
        inventoryRepository.saveAll(inventoryList);


        //TODO: Calculate dynamic amount

        log.info("creating the booking");
        Booking booking = Booking.builder()
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .roomCount(bookingRequest.getRoomsCount())
                .createdAt(LocalDateTime.now())
                .user(getCurrentUser())
                .status(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .amount(BigDecimal.TEN)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        return modelMapper.map(savedBooking, BookingDto.class);

    }

    @Override
    @Transactional
    public BookingDto addGuests(Long bookingId, List<GuestDto> guestDtoList) {
        log.info("Adding the guests for the Booking with Id {} ", bookingId);

        Booking booking = bookingRepository
                .findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id : " + bookingId));

        User currentUser = getCurrentUser();

        if(!currentUser.equals(booking.getUser())) {
            throw new UnAuthorizedException("Booking does not belong to the current user with the id : " + currentUser.getId());
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking expired");
        }
        if (booking.getStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking is not reserved, cannot add guests");
        }

        for (GuestDto guestDto : guestDtoList) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(currentUser);
            guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setStatus(BookingStatus.GUEST_ADDED);
        Booking savedBooking = bookingRepository.save(booking);
        return modelMapper.map(savedBooking, BookingDto.class);

    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreatedAt().plusMinutes(10).isBefore(LocalDateTime.now());
    }

    public User getCurrentUser() {
       return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
