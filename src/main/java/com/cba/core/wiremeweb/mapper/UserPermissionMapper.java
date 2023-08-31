package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.model.Permission;

public class UserPermissionMapper {

    public static PermissionResponseDto toDto(Permission entity) {
        PermissionResponseDto responseDto = new PermissionResponseDto();
        responseDto.setCreated(entity.getCreated() != 0);
        responseDto.setDeleted(entity.getDeleted() != 0);
        responseDto.setReadd(entity.getReadd() != 0);
        responseDto.setUpdated(entity.getUpdated() != 0);
        responseDto.setRole(entity.getRole().getRoleName());
        responseDto.setResource(entity.getResource().getName());
        return responseDto;
    }
}
