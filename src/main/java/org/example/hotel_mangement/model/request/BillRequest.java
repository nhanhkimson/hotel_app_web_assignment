package org.example.hotel_mangement.model.request;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class BillRequest {
    private UUID bookingId;
    private UUID guestId;
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



