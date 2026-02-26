package org.example.hotel_mangement.repository;

import java.util.List;
import java.util.UUID;

import org.example.hotel_mangement.model.entity.Employee;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    
    @EntityGraph(attributePaths = {"hotel", "role"})
    @Override
    List<Employee> findAll();

    @EntityGraph(attributePaths = {"hotel", "role"})
    List<Employee> findAllByOrderByCreatedAtDesc();
}
