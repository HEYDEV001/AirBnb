package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.HotelDto;
import com.Backend.Projects.AirBnb.entities.Hotel;
import com.Backend.Projects.AirBnb.exceptions.ResourceNotFoundException;
import com.Backend.Projects.AirBnb.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {


    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;

    @Override
    public HotelDto createHotel(HotelDto hotelDto) {
        log.info("creating a hotel with name {}", hotelDto.getName());
        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setIsActive(false);
        Hotel savedHotel = hotelRepository.save(hotel);
        log.info("Hotel created successfully  with ID {}", savedHotel.getId());
        return modelMapper.map(savedHotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("getting hotel with id {}", id);
        Hotel hotel= hotelRepository
                .findById(id.intValue())
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+id));
        return modelMapper.map(hotel, HotelDto.class);
    }



    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        Hotel hotel= hotelRepository
                .findById(id.intValue())
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+id));
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);
        log.info("Hotel updated successfully  with ID {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public void deleteHotelById(Long id) {
        Hotel hotel= hotelRepository
                .findById(id.intValue())
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+id));
        hotelRepository.deleteById(id.intValue());
        //TODO: delete the future inventories for this hotel
    }

    @Override
    public void activateHotel(Long id) {
        Hotel hotel= hotelRepository
                .findById(Math.toIntExact(id))
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+id));
        hotel.setIsActive(true);
        // TODO: create the inventory for this hotel
    }


}


