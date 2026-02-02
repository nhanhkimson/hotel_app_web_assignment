package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
}

