package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.model.Status;

public class RoleMapper {

    public static RoleResponseDto toDto(Role role) {
        RoleResponseDto roleResponseDto = new RoleResponseDto();
        roleResponseDto.setRoleName(role.getRoleName());
        roleResponseDto.setStatus(role.getStatus().getStatusCode());
        roleResponseDto.setId(role.getId());
        return roleResponseDto;
    }

    public static Role toModel(RoleRequestDto roleRequestDto) {
        Role role = new Role();
        role.setRoleName(roleRequestDto.getRoleName());
        role.setStatus(new Status(roleRequestDto.getStatus()));
        return role;
    }
}
