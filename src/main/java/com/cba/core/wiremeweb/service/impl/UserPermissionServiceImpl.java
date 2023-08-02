package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.impl.UserPermissionDaoImpl;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionDaoImpl userPermissionDaoImpl;

    @Override
    public List<PermissionResponseDto> findAll() throws SQLException {
        return userPermissionDaoImpl.findAll();
    }
}
