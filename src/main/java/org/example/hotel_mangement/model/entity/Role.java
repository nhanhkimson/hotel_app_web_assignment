package org.example.hotel_mangement.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

// 8. Role Entity
@Entity
@Table(name = "role")
@Data
public class Role {
    @Id
    @UuidGenerator
    @Column(name = "role_id", updatable = false, nullable = false, columnDefinition = "uuid")
    private UUID roleId;

    @Column(name = "role_title", nullable = false, length = 50)
    private String roleTitle;

    @Column(name = "role_desc", length = 200)
    private String roleDesc;

    @OneToMany(mappedBy = "role")
    private List<Employee> employees;
}