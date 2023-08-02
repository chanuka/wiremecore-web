package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.model.Permission;

public class UserPermissionMapper {

    public static PermissionResponseDto toDto(Permission permission){
        PermissionResponseDto permissionDto = new PermissionResponseDto();
        permissionDto.setCreated(permission.getCreated() != 0);
        permissionDto.setDeleted(permission.getDeleted() != 0);
        permissionDto.setReadd(permission.getReadd()!= 0);
        permissionDto.setUpdated(permission.getUpdated() != 0);
        permissionDto.setRole(permission.getRole().getRoleName());
        permissionDto.setResource(permission.getResource().getName());
        return permissionDto;
    }
}
