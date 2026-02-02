package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

// 2. Room Entity
@Entity
@Table(name = "room")
@Data
public class Room {
    @Id
    @UuidGenerator
    @Column(name = "room_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID roomId;

    @Column(name = "room_no", length = 10)
    private String roomNo;

    @ManyToOne
    @JoinColumn(name = "hotel_code", nullable = false, columnDefinition = "uuid")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_type_id", nullable = false, columnDefinition = "uuid")
    private RoomType roomType;

    @Column(length = 20)
    private String occupancy;

    @OneToMany(mappedBy = "room")
    @JsonIgnore
    private List<Booking> bookings;
}