package org.example.hotel_mangement.model.request;

import lombok.Data;

import java.util.UUID;

@Data
public class HotelRequest {
    private String hotelName;
    private String address;
    private String postcode;
    private String city;
    private String country;
    private Integer numRooms;
    private String phoneNo;
    private Integer starRating;
}

