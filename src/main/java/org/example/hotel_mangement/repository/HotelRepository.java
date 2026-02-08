package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.Hotel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface HotelRepository extends JpaRepository<Hotel, UUID> {
    
    @Query("SELECT h FROM Hotel h WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(h.hotelName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.city) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.country) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(h.address) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Hotel> searchHotels(@Param("search") String search, Pageable pageable);
}

