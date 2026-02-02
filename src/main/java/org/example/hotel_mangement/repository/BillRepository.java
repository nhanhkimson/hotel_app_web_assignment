package org.example.hotel_mangement.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    
    @EntityGraph(attributePaths = {"booking", "guest"})
    @Query("SELECT b FROM Bill b")
    Page<Bill> findAllWithRelations(Pageable pageable);
    
    @EntityGraph(attributePaths = {"booking", "guest"})
    Optional<Bill> findByInvoiceNo(UUID invoiceNo);
}

