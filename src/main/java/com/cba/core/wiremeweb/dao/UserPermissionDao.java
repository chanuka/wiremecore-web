package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface UserPermissionDao {

    public List<PermissionResponseDto> findAll() throws SQLException;
}
