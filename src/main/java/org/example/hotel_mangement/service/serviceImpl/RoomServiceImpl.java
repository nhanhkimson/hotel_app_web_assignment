package org.example.hotel_mangement.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.RoomDTO;
import org.example.hotel_mangement.model.entity.Hotel;
import org.example.hotel_mangement.model.entity.Room;
import org.example.hotel_mangement.model.entity.RoomType;
import org.example.hotel_mangement.model.request.RoomRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.HotelRepository;
import org.example.hotel_mangement.repository.RoomRepository;
import org.example.hotel_mangement.repository.RoomTypeRepository;
import org.example.hotel_mangement.service.RoomService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomTypeRepository roomTypeRepository;

    @Override
    public PayloadResponse<RoomDTO> findAll(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Room> rooms;
        if (search != null && !search.trim().isEmpty()) {
            rooms = roomRepository.searchRooms(search.trim(), pageable);
        } else {
            rooms = roomRepository.findAllWithRelations(pageable);
        }

        List<RoomDTO> roomDTOs = new ArrayList<>();
        if (!rooms.isEmpty()) {
            for (Room room : rooms) {
                roomDTOs.add(toDTO(room));
            }
        }

        return PayloadResponse.<RoomDTO>builder()
                .items(roomDTOs)
                .pagination(PaginationResponse.fromPage(rooms, page, size))
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String fieldName = mapSortField(sortBy);
        return Sort.by(direction, fieldName);
    }
    
    private String mapSortField(String sortBy) {
        switch (sortBy) {
            case "roomNo": return "roomNo";
            case "hotelName": return "hotel.hotelName";
            case "roomType": return "roomType.roomType";
            case "occupancy": return "occupancy";
            default: return "roomNo";
        }
    }

    @Override
    public RoomDTO findRoomById(UUID id) {
        Room room = getRoomById(id);
        return toDTO(room);
    }

    public Room getRoomById(UUID id) {
        return roomRepository.findByRoomId(id).orElseThrow(() -> new NotFoundException("Room not found."));
    }

    @Override
    public RoomDTO saveRoom(RoomRequest roomRequest) {
        Room room = new Room();
        return toDTO(saveOrUpdateRoom(room, roomRequest));
    }

    @Override
    public RoomDTO updateRoom(UUID id, RoomRequest roomRequest) {
        Room room = getRoomById(id);
        return toDTO(saveOrUpdateRoom(room, roomRequest));
    }

    @Override
    public RoomDTO deleteRoom(UUID id) {
        Room room = getRoomById(id);
        roomRepository.delete(room);
        return toDTO(room);
    }

    private Room saveOrUpdateRoom(Room room, RoomRequest roomRequest) {
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelCode())
                .orElseThrow(() -> new NotFoundException("Hotel not found."));
        RoomType roomType = roomTypeRepository.findById(roomRequest.getRoomTypeId())
                .orElseThrow(() -> new NotFoundException("Room type not found."));

        room.setRoomNo(roomRequest.getRoomNo());
        room.setHotel(hotel);
        room.setRoomType(roomType);
        room.setOccupancy(roomRequest.getOccupancy());
        return roomRepository.save(room);
    }

    private RoomDTO toDTO(Room room) {
        return RoomDTO.builder()
                .roomId(room.getRoomId())
                .roomNo(room.getRoomNo())
                .hotelCode(room.getHotel() != null ? room.getHotel().getHotelCode() : null)
                .hotelName(room.getHotel() != null ? room.getHotel().getHotelName() : null)
                .roomTypeId(room.getRoomType() != null ? room.getRoomType().getRoomTypeId() : null)
                .roomType(room.getRoomType() != null ? room.getRoomType().getRoomType() : null)
                .occupancy(room.getOccupancy())
                .build();
    }
}

