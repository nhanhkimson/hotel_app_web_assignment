package org.example.hotel_mangement.service;


import org.example.hotel_mangement.model.dto.RoomTypeDto;
import org.example.hotel_mangement.model.request.RoomTypeRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.List;
import java.util.UUID;

public interface RoomTypeService {
    PayloadResponse<RoomTypeDto> findAll(int page, int size);
    RoomTypeDto findRoomTypeById(UUID id);
    RoomTypeDto saveRoomType(RoomTypeRequest roomTypeRequest);
    RoomTypeDto updateRoomType(UUID id ,RoomTypeRequest roomTypeRequest);
    RoomTypeDto deleteRoomType(UUID id);
}
