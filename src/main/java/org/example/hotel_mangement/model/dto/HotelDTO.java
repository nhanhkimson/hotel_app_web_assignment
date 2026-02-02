package org.example.hotel_mangement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class HotelDTO {
    private UUID hotelCode;
    private String hotelName;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private Integer numRooms;
    private String phoneNo;
    private Integer starRating;
}

