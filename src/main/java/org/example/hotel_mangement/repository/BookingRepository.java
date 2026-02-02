package org.example.hotel_mangement.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
    @EntityGraph(attributePaths = {"hotel", "guest", "room"})
    @Query("SELECT b FROM Booking b")
    Page<Booking> findAllWithRelations(Pageable pageable);
    
    @EntityGraph(attributePaths = {"hotel", "guest", "room"})
    Optional<Booking> findByBookingId(UUID bookingId);
}

