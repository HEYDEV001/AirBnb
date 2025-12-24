package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.HotelDto;
import com.Backend.Projects.AirBnb.dto.HotelPriceDto;
import com.Backend.Projects.AirBnb.dto.HotelSearchDto;
import com.Backend.Projects.AirBnb.entities.Hotel;
import com.Backend.Projects.AirBnb.entities.Inventory;
import com.Backend.Projects.AirBnb.entities.Room;
import com.Backend.Projects.AirBnb.repository.HotelMinPriceRepository;
import com.Backend.Projects.AirBnb.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ModelMapper modelMapper;
    private final HotelMinPriceRepository hotelMinPriceRepository;

    @Override
    public void InitialiseInventory(Room room) {

        LocalDate today = LocalDate.now();
        LocalDate endDate = today.plusYears(1);
        for (; !today.isAfter(endDate); today = today.plusDays(1)) {
            Inventory inventory = Inventory.builder()
                    .hotel(room.getHotel())
                    .room(room)
                    .bookedCount(0)
                    .reservedCount(0)
                    .city(room.getHotel().getCity())
                    .date(today)
                    .price(room.getBasePrice())
                    .surgeFactor(BigDecimal.ONE)
                    .totalCount(room.getTotalCount())
                    .closed(false)
                    .build();
            inventoryRepository.save(inventory);
        }
    }


    @Override
    public Page<HotelPriceDto> searchHotels(HotelSearchDto hotelSearchDto) {
        log.info("trying to search hotel in {} form {} to {}", hotelSearchDto.getStartDate(), hotelSearchDto.getEndDate(), hotelSearchDto.getCity());
        Pageable pageable = PageRequest.of(hotelSearchDto.getPage(), hotelSearchDto.getSize());
        long dateCount = ChronoUnit.DAYS.between(
                hotelSearchDto.getStartDate(),
                hotelSearchDto.getEndDate()
        )+1;
        Page<HotelPriceDto> hotels = hotelMinPriceRepository.findHotelsWithAvailableInventories(
                hotelSearchDto.getCity(),
                hotelSearchDto.getStartDate(),
                hotelSearchDto.getEndDate(),
                hotelSearchDto.getRoomsCount(),
                dateCount,
                pageable
        );
        return hotels;


    }

    @Override
    public void deleteAllInventory(Room room) {
        log.info("Deleting all inventory for the room with id {}", room.getId());
        inventoryRepository.deleteByRoom(room);
    }
}
