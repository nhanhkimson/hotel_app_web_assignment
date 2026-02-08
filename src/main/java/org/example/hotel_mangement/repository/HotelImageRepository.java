package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.HotelImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HotelImageRepository extends JpaRepository<HotelImage, UUID> {
    List<HotelImage> findByHotel_HotelCode(UUID hotelCode);
}

