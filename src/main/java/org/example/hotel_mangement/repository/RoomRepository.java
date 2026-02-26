package org.example.hotel_mangement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    
    @EntityGraph(attributePaths = {"hotel", "roomType"})
    @Query("SELECT r FROM Room r WHERE (r.active = true OR r.active IS NULL)")
    Page<Room> findAllWithRelations(Pageable pageable);

    @EntityGraph(attributePaths = {"hotel", "roomType"})
    @Query("SELECT r FROM Room r WHERE (r.active = true OR r.active IS NULL) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(r.roomNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.hotel.hotelName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.roomType.roomType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.occupancy) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Room> searchRooms(@Param("search") String search, Pageable pageable);
    
    @EntityGraph(attributePaths = {"hotel", "roomType"})
    Optional<Room> findByRoomId(UUID roomId);
    
    @EntityGraph(attributePaths = {"hotel", "roomType"})
    List<Room> findByHotel_HotelCode(UUID hotelCode);
}

