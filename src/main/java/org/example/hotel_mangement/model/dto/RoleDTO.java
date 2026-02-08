package org.example.hotel_mangement.model.dto;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
public class RoleDTO {
    private UUID roleId;
    private String roleTitle;
    private String roleDesc;
}


