package org.example.hotel_mangement.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.request.RoleRequest;
import org.example.hotel_mangement.model.response.ApiResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/role")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<ApiResponse<PayloadResponse<RoleDTO>>> getRoles(
            @RequestParam(defaultValue = "1") @Min(value = 1, message = "Page number must be at least 1") int page,
            @RequestParam(defaultValue = "10") @Min(value = 1, message = "Size must be at least 1") int size
    ) {
        ApiResponse<PayloadResponse<RoleDTO>> rolesApiResponse = ApiResponse.<PayloadResponse<RoleDTO>>builder()
                .message("Get roles")
                .status(HttpStatus.OK)
                .payload(roleService.findAll(page, size))
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

