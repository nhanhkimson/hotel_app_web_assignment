package org.example.hotel_mangement.model.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RoomRequest {
    private String roomNo;
    private UUID hotelCode;
    private UUID roomTypeId;
    private String occupancy;
}


