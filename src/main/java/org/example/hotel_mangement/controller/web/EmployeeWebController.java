package org.example.hotel_mangement.controller.web;

import org.example.hotel_mangement.service.EmployeeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/web/employees")
@RequiredArgsConstructor
public class EmployeeWebController {
    private final EmployeeService employeeService;

    @GetMapping
    public String listEmployees(Model model) {
        model.addAttribute("employees", employeeService.getEmployee());
        model.addAttribute("activePage", "employees");
        model.addAttribute("pageTitle", "Employees - Hotel Management");
        return "web/employees/list";
    }
}
