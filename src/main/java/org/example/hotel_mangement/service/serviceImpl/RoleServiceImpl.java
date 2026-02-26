package org.example.hotel_mangement.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.example.hotel_mangement.exception.NotFoundException;
import org.example.hotel_mangement.model.dto.RoleDTO;
import org.example.hotel_mangement.model.entity.Role;
import org.example.hotel_mangement.model.request.RoleRequest;
import org.example.hotel_mangement.model.response.PaginationResponse;
import org.example.hotel_mangement.model.response.PayloadResponse;
import org.example.hotel_mangement.repository.RoleRepository;
import org.example.hotel_mangement.service.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public PayloadResponse<RoleDTO> findAll(int page, int size, String search, String sortBy, String sortDir) {
        Sort sort = createSort(sortBy, sortDir);
        PageRequest pageable = PageRequest.of(page - 1, size, sort);
        
        Page<Role> roles;
        if (search != null && !search.trim().isEmpty()) {
            roles = roleRepository.searchRoles(search.trim(), pageable);
        } else {
            roles = roleRepository.findByActiveTrueOrActiveIsNull(pageable);
        }

        List<RoleDTO> roleDTOs = new ArrayList<>();
        for (Role role : roles) {
            roleDTOs.add(toDTO(role));
        }

        return PayloadResponse.<RoleDTO>builder()
                .items(roleDTOs)
                .pagination(PaginationResponse.fromPage(roles, page, size))
                .build();
    }
    
    private Sort createSort(String sortBy, String sortDir) {
        Sort.Direction direction = "desc".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        String fieldName = mapSortField(sortBy);
        return Sort.by(direction, fieldName);
    }
    
    private String mapSortField(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) return "createdAt";
        switch (sortBy) {
            case "createdAt": return "createdAt";
            case "roleTitle": return "roleTitle";
            case "roleDesc": return "roleDesc";
            default: return "createdAt";
        }
    }

    @Override
    public RoleDTO findRoleById(UUID id) {
        Role role = getRoleById(id);
        return toDTO(role);
    }

    public Role getRoleById(UUID id) {
        return roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found."));
    }

    @Override
    public RoleDTO saveRole(RoleRequest roleRequest) {
        Role role = new Role();
        return toDTO(saveOrUpdateRole(role, roleRequest));
    }

    @Override
    public RoleDTO updateRole(UUID id, RoleRequest roleRequest) {
        Role role = getRoleById(id);
        return toDTO(saveOrUpdateRole(role, roleRequest));
    }

    @Override
    public RoleDTO deleteRole(UUID id) {
        Role role = getRoleById(id);
        role.setActive(false);
        roleRepository.save(role);
        return toDTO(role);
    }

    private Role saveOrUpdateRole(Role role, RoleRequest roleRequest) {
        role.setRoleTitle(roleRequest.getRoleTitle());
        role.setRoleDesc(roleRequest.getRoleDesc());
        return roleRepository.save(role);
    }

    private RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .roleId(role.getRoleId())
                .roleTitle(role.getRoleTitle())
                .roleDesc(role.getRoleDesc())
                .build();
    }
}

