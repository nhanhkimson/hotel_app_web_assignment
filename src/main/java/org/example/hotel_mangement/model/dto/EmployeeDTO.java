package org.example.hotel_mangement.model.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class EmployeeDTO {
    private UUID employeeId;
    private UUID hotelCode; // Just the ID
    private String hotelName; // For display
    private RoleDTO role; // Use RoleDTO instead of entity
    private String firstName;
    private String lastName;
    private Date dob;
    private String gender;
    private String phoneNo;
    private String email;
    // password excluded for security
    private Double salary;
}
