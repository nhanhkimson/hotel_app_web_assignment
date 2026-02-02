package org.example.hotel_mangement.controller;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.model.dto.EmployeeDTO;
import org.example.hotel_mangement.model.entity.Employee;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ApiResponse<List<EmployeeDTO>> getEmployee(){
        ApiResponse<List<EmployeeDTO>> apiResponse = ApiResponse.<List<EmployeeDTO>>builder()
                .message("Get Employee Success.")
                .status(HttpStatus.OK)
                .payload(employeeService.getEmployee())
                .build();
        return apiResponse;
    }
}
