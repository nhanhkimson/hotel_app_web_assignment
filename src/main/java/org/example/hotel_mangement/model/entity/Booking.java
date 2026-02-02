package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

// 5. Booking Entity
@Entity
@Table(name = "booking")
@Data
public class Booking {
    @Id
   @UuidGenerator
    @Column(name = "booking_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID bookingId;

    @ManyToOne
    @JoinColumn(name = "hotel_code", nullable = false, columnDefinition = "uuid")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "guest_id", nullable = false, columnDefinition = "uuid")
    private Guest guest;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false, columnDefinition = "uuid")
    private Room room;

    @Column(name = "booking_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date bookingDate;

    @Column(name = "booking_time")
    @Temporal(TemporalType.TIME)
    private java.util.Date bookingTime;

    @Column(name = "arrival_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date arrivalDate;

    @Column(name = "departure_date")
    @Temporal(TemporalType.DATE)
    private java.util.Date departureDate;

    @Column(name = "est_arrival_time")
    @Temporal(TemporalType.TIME)
    private java.util.Date estArrivalTime;

    @Column(name = "est_departure_time")
    @Temporal(TemporalType.TIME)
    private java.util.Date estDepartureTime;

    @Column(name = "num_adults")
    private Integer numAdults;

    @Column(name = "num_children")
    private Integer numChildren;

    @Column(name = "special_req", length = 500)
    private String specialReq;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    @JsonIgnore
    private Guest guestDetails;

    @OneToMany(mappedBy = "booking")
    @JsonIgnore
    private List<Bill> bills;
}