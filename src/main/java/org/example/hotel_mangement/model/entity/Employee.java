package org.example.hotel_mangement.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;
import jakarta.persistence.PrePersist;

// 7. Employee Entity
@Entity
@Table(name = "employee")
@Data
public class Employee {
    @Id
    @UuidGenerator
    @Column(name = "employee_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID employeeId;

    @ManyToOne
    @JoinColumn(name = "hotel_code", nullable = false, columnDefinition = "uuid")
    @JsonIgnore
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, columnDefinition = "uuid")
    private Role role;

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

    @Column(length = 100, unique = true)
    private String email;

    @Column(length = 100)
    private String password;

    @Column
    private Double salary;

    @Column(name = "active", nullable = true)
    private Boolean active = true;

    @Column(name = "created_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
        if (active == null) active = true;
    }
}