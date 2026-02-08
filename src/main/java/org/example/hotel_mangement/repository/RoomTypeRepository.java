package org.example.hotel_mangement.repository;


import org.example.hotel_mangement.model.entity.RoomType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, UUID> {
    
    @Query("SELECT rt FROM RoomType rt WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(rt.roomType) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(rt.roomDesc) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<RoomType> searchRoomTypes(@Param("search") String search, Pageable pageable);
}
