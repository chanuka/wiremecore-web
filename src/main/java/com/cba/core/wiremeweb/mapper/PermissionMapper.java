package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.PermissionRequestDto;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.model.Permission;
import com.cba.core.wiremeweb.model.Resource;
import com.cba.core.wiremeweb.model.Role;

public class PermissionMapper {

    public static PermissionResponseDto toDto(Permission entity) {
        PermissionResponseDto responseDto = new PermissionResponseDto();
        responseDto.setId(entity.getId());
        responseDto.setCreated(entity.getCreated().intValue());
        responseDto.setDeleted(entity.getDeleted().intValue());
        responseDto.setReadd(entity.getReadd().intValue());
        responseDto.setUpdated(entity.getUpdated().intValue());
        responseDto.setRoleId(entity.getRole().getId());
        responseDto.setRoleName(entity.getRole().getRoleName());
        responseDto.setResourceId(entity.getResource().getId());
        responseDto.setResourceName(entity.getResource().getName());
        return responseDto;
    }

    public static Permission toModel(PermissionRequestDto requestDto) {
        Permission entity = new Permission();
        entity.setRole(new Role(requestDto.getRoleId()));
        entity.setResource(new Resource(requestDto.getResourceId()));
        entity.setCreated(requestDto.getCreated().byteValue());
        entity.setUpdated(requestDto.getUpdated().byteValue());
        entity.setDeleted(requestDto.getDeleted().byteValue());
        entity.setReadd(requestDto.getReadd().byteValue());
        return entity;
    }
}
