package com.Backend.Projects.AirBnb.controller;

import com.Backend.Projects.AirBnb.dto.HotelDto;
import com.Backend.Projects.AirBnb.service.HotelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/admin/hotels")
@RequiredArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @PostMapping
    public ResponseEntity<HotelDto> createHotel(@RequestBody HotelDto hotelDto) {
        log.info("attempting to create Hotel");
        HotelDto hotelDto1 = hotelService.createHotel(hotelDto);
        return new ResponseEntity<>(hotelDto1, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HotelDto> getHotelById(@PathVariable Long id) {
        log.info("attempting to get Hotel");
        HotelDto hotelDto = hotelService.getHotelById(id);
        return new ResponseEntity<>(hotelDto, HttpStatus.FOUND);
    }


    @PutMapping("/{id}")
    public ResponseEntity<HotelDto> updateHotelById(@PathVariable Long id, @RequestBody HotelDto hotelDto) {
        log.info("attempting to update Hotel");
        HotelDto hotel = hotelService.updateHotelById(id,hotelDto);
        return ResponseEntity.ok(hotel);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotelById(@PathVariable Long id) {
        log.info("attempting to delete Hotel");
       hotelService.deleteHotelById(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> activateHotel(@PathVariable Long id) {
        log.info("attempting to activate Hotel");
        hotelService.activateHotel(id);
        return ResponseEntity.noContent().build();
    }

}
