package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.model.Permission;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao<T, K> extends GenericDao<T, K> {

    Iterable<Permission> findAllPermissionsByUser(String username) throws SQLException;

}
