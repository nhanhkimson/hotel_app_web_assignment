package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.BookingImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BookingImageRepository extends JpaRepository<BookingImage, UUID> {
    List<BookingImage> findByBooking_BookingId(UUID bookingId);
}


