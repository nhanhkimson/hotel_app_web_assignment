package org.example.hotel_mangement.service;

import java.util.UUID;

import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.request.RoleRequest;
import org.example.hotel_mangement.model.response.PayloadResponse;

public interface RoleService {
    PayloadResponse<RoleDTO> findAll(int page, int size, String search, String sortBy, String sortDir);
    RoleDTO findRoleById(UUID id);
    RoleDTO saveRole(RoleRequest roleRequest);
    RoleDTO updateRole(UUID id, RoleRequest roleRequest);
    RoleDTO deleteRole(UUID id);
}

