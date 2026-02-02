package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.RoomDTO;
import org.example.hotel_mangement.model.request.RoomRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.UUID;

public interface RoomService {
    PayloadResponse<RoomDTO> findAll(int page, int size);
    RoomDTO findRoomById(UUID id);
    RoomDTO saveRoom(RoomRequest roomRequest);
    RoomDTO updateRoom(UUID id, RoomRequest roomRequest);
    RoomDTO deleteRoom(UUID id);
}

