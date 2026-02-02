package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
}

