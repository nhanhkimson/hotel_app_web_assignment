package org.example.hotel_mangement.service.serviceImpl;

import java.util.ArrayList;
import java.util.List;

import org.example.hotel_mangement.model.dto.EmployeeDTO;
import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.entity.Employee;
import org.example.hotel_mangement.repository.EmployeeRepository;
import org.example.hotel_mangement.service.EmployeeService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public List<EmployeeDTO> getEmployee() {
        List<EmployeeDTO> employeeDTO  = new ArrayList<>();
        List<Employee> employees = employeeRepository.findAllByOrderByCreatedAtDesc();
        for (Employee employee : employees) {
            employeeDTO.add(employeeToDTO(employee));
        }
        return employeeDTO;
    }

    private EmployeeDTO employeeToDTO(Employee employee) {
        RoleDTO roleDTO = null;
        if (employee.getRole() != null) {
            roleDTO = RoleDTO.builder()
                    .roleId(employee.getRole().getRoleId())
                    .roleTitle(employee.getRole().getRoleTitle())
                    .roleDesc(employee.getRole().getRoleDesc())
                    .build();
        }
        
        return EmployeeDTO.builder()
                .employeeId(employee.getEmployeeId())
                .hotelCode(employee.getHotel() != null ? employee.getHotel().getHotelCode() : null)
                .hotelName(employee.getHotel() != null ? employee.getHotel().getHotelName() : null)
                .role(roleDTO)
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .gender(employee.getGender())
                .email(employee.getEmail())
                .salary(employee.getSalary())
                .dob(employee.getDob())
                .phoneNo(employee.getPhoneNo())
                .build();
    }
}
