package org.example.hotel_mangement.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
    
    @EntityGraph(attributePaths = {"hotel", "guest", "room"})
    @Query("SELECT b FROM Booking b")
    Page<Booking> findAllWithRelations(Pageable pageable);
    
    @EntityGraph(attributePaths = {"hotel", "guest", "room"})
    @Query("SELECT b FROM Booking b WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(b.guest.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.guest.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.hotel.hotelName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.room.roomNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "CAST(b.bookingId AS string) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Booking> searchBookings(@Param("search") String search, Pageable pageable);
    
    @EntityGraph(attributePaths = {"hotel", "guest", "room"})
    Optional<Booking> findByBookingId(UUID bookingId);
}

