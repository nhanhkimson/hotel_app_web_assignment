package org.example.hotel_mangement.model.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BookingRequest {
    private UUID hotelCode;
    private UUID guestId;
    private UUID roomId;
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

