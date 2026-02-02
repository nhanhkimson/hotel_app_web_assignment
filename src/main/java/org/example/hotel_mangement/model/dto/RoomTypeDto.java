package org.example.hotel_mangement.model.dto;


import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RoomTypeDto {

    private UUID roomTypeId;
    private String roomType;
    private Double roomPrice;
    private Double defaultRoomPrice;
    private String roomImg;
    private String roomDesc;
}
