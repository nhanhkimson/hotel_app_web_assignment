package org.example.hotel_mangement.repository;

import org.example.hotel_mangement.model.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    
    @Query("SELECT r FROM Role r WHERE " +
           "(:search IS NULL OR :search = '' OR " +
           "LOWER(r.roleTitle) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(r.roleDesc) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Role> searchRoles(@Param("search") String search, Pageable pageable);
}

