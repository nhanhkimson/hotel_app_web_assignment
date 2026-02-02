package org.example.hotel_mangement.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    
    @EntityGraph(attributePaths = {"hotel", "roomType"})
    @Query("SELECT r FROM Room r")
    Page<Room> findAllWithRelations(Pageable pageable);
    
    @EntityGraph(attributePaths = {"hotel", "roomType"})
    Optional<Room> findByRoomId(UUID roomId);
}

