package org.example.hotel_mangement.model.dto;

import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RoomDTO {
    private UUID roomId;
    private String roomNo;
    private UUID hotelCode; // Just the ID
    private String hotelName; // For display
    private UUID roomTypeId; // Just the ID
    private String roomType; // For display
    private String occupancy;
}

