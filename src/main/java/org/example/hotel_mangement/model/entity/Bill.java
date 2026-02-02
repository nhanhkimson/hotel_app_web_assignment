package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

// 6. Bill Entity
@Entity
@Table(name = "bill")
@Data
public class Bill {
    @Id
    @UuidGenerator
    @Column(name = "invoice_no", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID invoiceNo;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false, columnDefinition = "uuid")
    @JsonIgnore
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false, columnDefinition = "uuid")
    @JsonIgnore
    private Guest guest;

    @Column(name = "room_charge")
    private Double roomCharge;

    @Column(name = "room_service")
    private Double roomService;

    @Column(name = "restaurant_charges")
    private Double restaurantCharges;

    @Column(name = "bar_charges")
    private Double barCharges;

    @Column(name = "misc_charges")
    private Double miscCharges;

    @Column(name = "if_late_checkout")
    private Boolean ifLateCheckout;

    @Column(name = "payment_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date paymentDate;

    @Column(name = "payment_mode", length = 50)
    private String paymentMode;

    @Column(name = "credit_card_no", length = 20)
    private String creditCardNo;

    @Column(name = "expire_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date expireDate;

    @Column(name = "cheque_no", length = 50)
    private String chequeNo;
}