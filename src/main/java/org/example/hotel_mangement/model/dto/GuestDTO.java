package org.example.hotel_mangement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Builder
@Data
public class GuestDTO {
    private UUID guestId;
    private UUID bookingId; // Just the ID, not the full object
    private String guestTitle;
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String phoneNo;
    private String email;
    // password excluded for security
    private String passportNo;
    private String address;
    private String postcode;
    private String city;
    private String country;
}


