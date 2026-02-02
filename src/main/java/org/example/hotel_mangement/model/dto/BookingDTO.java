package org.example.hotel_mangement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class BookingDTO {
    private UUID bookingId;
    private UUID hotelCode; // Just the ID
    private String hotelName; // For display
    private UUID guestId; // Just the ID
    private String guestName; // For display
    private UUID roomId; // Just the ID
    private String roomNo; // For display
    private Date bookingDate;
    private Date bookingTime;
    private Date arrivalDate;
    private Date departureDate;
    private Date estArrivalTime;
    private Date estDepartureTime;
    private Integer numAdults;
    private Integer numChildren;
    private String specialReq;
}

