package org.example.hotel_mangement.repository;

import java.util.Optional;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    
    @EntityGraph(attributePaths = {"booking", "guest"})
    @Query("SELECT b FROM Bill b WHERE (b.active = true OR b.active IS NULL)")
    Page<Bill> findAllWithRelations(Pageable pageable);

    @EntityGraph(attributePaths = {"booking", "guest"})
    @Query("SELECT b FROM Bill b WHERE (b.active = true OR b.active IS NULL) AND " +
           "(:search IS NULL OR :search = '' OR " +
           "CAST(b.invoiceNo AS string) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.guest.firstName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.guest.lastName) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(b.paymentMode) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Bill> searchBills(@Param("search") String search, Pageable pageable);
    
    @EntityGraph(attributePaths = {"booking", "guest"})
    Optional<Bill> findByInvoiceNo(UUID invoiceNo);
}

