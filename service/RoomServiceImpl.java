package com.Backend.Projects.AirBnb.service;

import com.Backend.Projects.AirBnb.dto.HotelDto;
import com.Backend.Projects.AirBnb.dto.RoomDto;
import com.Backend.Projects.AirBnb.entities.Hotel;
import com.Backend.Projects.AirBnb.entities.Room;
import com.Backend.Projects.AirBnb.exceptions.ResourceNotFoundException;
import com.Backend.Projects.AirBnb.repository.HotelRepository;
import com.Backend.Projects.AirBnb.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {


    private final ModelMapper modelMapper;
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;


    @Override
    public RoomDto createRoom(Long hotelId,RoomDto roomDto) {
        log.info("creating a room in hotel with id  {}",hotelId);
        Hotel hotel= hotelRepository
                .findById(hotelId.intValue())
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+hotelId));
        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);
        // TODO: create inventory as soon as the room is created and the hotel is active
        log.info("Room created successfully  with ID {}", room.getId());
        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public RoomDto getRoomById(Long hotelId, Long roomId) {
        log.info("Getting room with id {} in hotel {}", roomId, hotelId);

        // Fetch the hotel
        Hotel hotel = hotelRepository
                .findById(hotelId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel not found with id: " + hotelId));

        // Fetch the room that belongs to this hotel
        Room room = roomRepository.findById(roomId.intValue())
                .filter(r -> r.getHotel().getId().equals(hotel.getId()))
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Room not found with id " + roomId + " for hotel id " + hotelId));

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRooms(Long hotelId) {
        log.info("getting all the rooms in hotel with id  {}",hotelId);
        Hotel hotel= hotelRepository
                .findById(hotelId.intValue())
                .orElseThrow( () -> new ResourceNotFoundException("hotel not found with the id:"+hotelId));

       return hotel.getRooms()
               .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteRoomById(Long roomId) {
        Room room = roomRepository
                .findById(roomId.intValue())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id " + roomId));

        roomRepository.deleteById(roomId.intValue());
        log.info("Room deleted successfully  with ID {}", roomId);
        // TODO: delete the future inventory for this room
    }
}
