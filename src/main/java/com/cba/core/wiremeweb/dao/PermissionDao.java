package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.Permission;

import java.sql.SQLException;
import java.util.List;

public interface PermissionDao<T> extends GenericDao<T> {

    Iterable<Permission> findAllPermissionsByUser(String username) throws SQLException;

    void deleteByRole_Id(int roleId) throws SQLException;

    List<Permission> findAllByRole_Id(int id) throws Exception;
}
