package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao<T, K> extends GenericDao<T, K> {

    List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException;

    List<PermissionResponseDto> findAllPermissionsByUser() throws SQLException;
}
