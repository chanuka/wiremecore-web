package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.Permission;

import java.sql.SQLException;

public interface PermissionDao<T, K> extends GenericDao<T, K> {

    Iterable<Permission> findAllPermissionsByUser(String username) throws SQLException;

}
