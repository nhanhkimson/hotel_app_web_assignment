package org.example.hotel_mangement.model.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String guestTitle;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String phoneNo;
    private String passportNo;
    private String address;
    private String city;
    private String country;
}