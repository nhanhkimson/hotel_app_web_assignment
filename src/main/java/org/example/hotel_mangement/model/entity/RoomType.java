package org.example.hotel_mangement.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.PrePersist;

@Entity
@Table(name = "room_type")
@Data
public class RoomType {
    @Id
    @UuidGenerator
    @Column(name = "room_type_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID roomTypeId;

    @Column(name = "room_type", length = 50)
    private String roomType;

    @Column(name = "room_price")
    private Double roomPrice;

    @Column(name = "default_room_price")
    private Double defaultRoomPrice;

    @Column(name = "room_img", length = 200)
    private String roomImg;

    @Column(name = "room_desc", length = 500)
    private String roomDesc;

    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @OneToMany(mappedBy = "roomType")
    private List<Room> rooms;
}