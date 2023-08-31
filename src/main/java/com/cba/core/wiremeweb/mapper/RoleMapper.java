package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.model.Status;

public class RoleMapper {

    public static RoleResponseDto toDto(Role entity) {
        RoleResponseDto responseDto = new RoleResponseDto();
        responseDto.setRoleName(entity.getRoleName());
        responseDto.setStatus(entity.getStatus().getStatusCode());
        responseDto.setId(entity.getId());
        return responseDto;
    }

    public static Role toModel(RoleRequestDto requestDto) {
        Role entity = new Role();
        entity.setRoleName(requestDto.getRoleName());
        entity.setStatus(new Status(requestDto.getStatus()));
        return entity;
    }
}
