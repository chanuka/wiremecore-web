package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface PermissionService<T, K> extends GenericService<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws Exception;

}
