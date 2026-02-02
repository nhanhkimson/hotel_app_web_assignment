package org.example.hotel_mangement.model.dto;

import java.util.Date;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class BillDTO {
    private UUID invoiceNo;
    private UUID bookingId; // Just the ID
    private UUID guestId; // Just the ID
    private String guestName; // For display
    private Double roomCharge;
    private Double roomService;
    private Double restaurantCharges;
    private Double barCharges;
    private Double miscCharges;
    private Boolean ifLateCheckout;
    private Date paymentDate;
    private String paymentMode;
    private String creditCardNo;
    private Date expireDate;
    private String chequeNo;
}

