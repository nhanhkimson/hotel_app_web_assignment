package org.example.hotel_mangement.model.request;
import lombok.Data;

@Data
public class RoomTypeRequest {
    private String roomType;
    private Double roomPrice;
    private Double defaultRoomPrice;
    private String roomImg;
    private String roomDesc;
}