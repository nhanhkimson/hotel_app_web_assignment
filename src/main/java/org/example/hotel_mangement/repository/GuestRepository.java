package org.example.hotel_mangement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Guest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, UUID> {
    
    @EntityGraph(attributePaths = {"booking"})
    @Override
    List<Guest> findAll();
    
    @EntityGraph(attributePaths = {"booking"})
    @Query("SELECT g FROM Guest g WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(g.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(g.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(g.email) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(g.phoneNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(g.country) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Guest> searchGuests(@Param("search") String search, Pageable pageable);
    
    @EntityGraph(attributePaths = {"booking"})
    @Override
    Optional<Guest> findById(UUID id);
    
    @EntityGraph(attributePaths = {"booking"})
    Optional<Guest> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
