package org.example.hotel_mangement.service;

import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.request.RoleRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

import java.util.UUID;

public interface RoleService {
    PayloadResponse<RoleDTO> findAll(int page, int size);
    RoleDTO findRoleById(UUID id);
    RoleDTO saveRole(RoleRequest roleRequest);
    RoleDTO updateRole(UUID id, RoleRequest roleRequest);
    RoleDTO deleteRole(UUID id);
}

