package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.PermissionResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface UserPermissionService {

    public List<PermissionResponseDto> findAll() throws SQLException;
}
