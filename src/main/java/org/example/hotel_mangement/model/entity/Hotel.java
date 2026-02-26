package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "hotel")
@Data
public class Hotel {
    @Id
    @UuidGenerator
    @Column(name = "hotel_code", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID hotelCode;

    @Column(name = "hotel_name", nullable = false, length = 100)
    private String hotelName;

    @Column(length = 200)
    private String address;

    @Column(length = 20)
    private String postcode;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(name = "num_rooms")
    private Integer numRooms;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(name = "star_rating")
    private Integer starRating;

    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Room> rooms;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Employee> employees;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Booking> bookings;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<org.example.hotel_mangement.model.entity.HotelImage> images;
}