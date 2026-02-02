package org.example.hotel_mangement.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Guest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuestRepository extends JpaRepository<Guest, UUID> {
    
    @EntityGraph(attributePaths = {"booking"})
    @Override
    List<Guest> findAll();
    
    @EntityGraph(attributePaths = {"booking"})
    @Override
    Optional<Guest> findById(UUID id);
    
    @EntityGraph(attributePaths = {"booking"})
    Optional<Guest> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
