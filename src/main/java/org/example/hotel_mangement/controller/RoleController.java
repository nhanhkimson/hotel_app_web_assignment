package org.example.hotel_mangement.controller;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.request.RoleRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<RoleDTO>>> getRoles(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "roleTitle") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        ApiResponse<PayloadResponse<RoleDTO>> rolesApiResponse = ApiResponse.<PayloadResponse<RoleDTO>>builder()
                .message("Get roles")
                .status(HttpStatus.OK)
                .payload(roleService.findAll(page, size, search, sortBy, sortDir))
                .build();
        return ResponseEntity.ok(rolesApiResponse);
    }

    @GetMapping("{role-id}")
    public ResponseEntity<ApiResponse<RoleDTO>> getRoleById(@PathVariable("role-id") UUID id) {
        ApiResponse<RoleDTO> roleDtoApiResponse = ApiResponse.<RoleDTO>builder()
                .message("Get role by id")
                .status(HttpStatus.OK)
                .payload(roleService.findRoleById(id))
                .build();
        return ResponseEntity.ok(roleDtoApiResponse);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RoleDTO>> createRole(@RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleDTO> roleDtoApiResponse = ApiResponse.<RoleDTO>builder()
                .message("Add role success.")
                .status(HttpStatus.CREATED)
                .payload(roleService.saveRole(roleRequest))
                .build();
        return ResponseEntity.ok(roleDtoApiResponse);
    }

    @PutMapping("{role-id}")
    public ResponseEntity<ApiResponse<RoleDTO>> updateRole(@PathVariable("role-id") UUID id, @RequestBody RoleRequest roleRequest) {
        ApiResponse<RoleDTO> roleDtoApiResponse = ApiResponse.<RoleDTO>builder()
                .message("Update role success.")
                .status(HttpStatus.OK)
                .payload(roleService.updateRole(id, roleRequest))
                .build();
        return ResponseEntity.ok(roleDtoApiResponse);
    }

    @DeleteMapping("{role-id}")
    public ResponseEntity<ApiResponse<RoleDTO>> deleteRole(@PathVariable("role-id") UUID id) {
        ApiResponse<RoleDTO> roleDtoApiResponse = ApiResponse.<RoleDTO>builder()
                .message("Deleted role success.")
                .status(HttpStatus.OK)
                .payload(roleService.deleteRole(id))
                .build();
        return ResponseEntity.ok(roleDtoApiResponse);
    }
}

