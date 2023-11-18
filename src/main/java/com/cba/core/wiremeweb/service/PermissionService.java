package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;

import java.util.List;

public interface PermissionService<T, K> extends GenericService<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws Exception;

}
