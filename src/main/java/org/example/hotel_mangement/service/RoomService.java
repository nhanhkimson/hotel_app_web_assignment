package org.example.hotel_mangement.service;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoomDTO;
import org.example.hotel_mangement.model.request.RoomRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

public interface RoomService {
    PayloadResponse<RoomDTO> findAll(int page, int size, String search, String sortBy, String sortDir);
    RoomDTO findRoomById(UUID id);
    RoomDTO saveRoom(RoomRequest roomRequest);
    RoomDTO updateRoom(UUID id, RoomRequest roomRequest);
    RoomDTO deleteRoom(UUID id);
}

