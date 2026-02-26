package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.PrePersist;

// 4. Guest Entity
@Entity
@Table(name = "guest")
@Data
public class Guest {
    @Id
    @UuidGenerator
    @Column(name = "guest_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID guestId;

    @OneToOne
    @JoinColumn(name = "booking_id", columnDefinition = "uuid")
    @JsonIgnore
    private Booking booking;

    @Column(name = "guest_title", length = 10)
    private String guestTitle;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private java.util.Date dob;

    @Column(length = 10)
    private String gender;

    @Column(name = "phone_no", length = 20)
    private String phoneNo;

    @Column(length = 100)
    private String email;

    @Column(length = 100)
    private String password;

    @Column(name = "passport_no", length = 50)
    private String passportNo;

    @Column(length = 200)
    private String address;

    @Column(length = 20)
    private String postcode;

    @Column(length = 50)
    private String city;

    @Column(length = 50)
    private String country;

    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    @OneToMany(mappedBy = "guest")
    @JsonIgnore
    private List<Bill> bills;
}