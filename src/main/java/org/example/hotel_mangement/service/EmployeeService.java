package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.EmployeeDTO;
import org.example.hotel_mangement.model.entity.Employee;

import java.util.List;

public interface EmployeeService {
    List<EmployeeDTO> getEmployee();
}
