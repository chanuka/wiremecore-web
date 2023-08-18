package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.UserPermissionDao;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.service.UserPermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserPermissionServiceImpl implements UserPermissionService {

    private final UserPermissionDao userPermissionDao;

    @Override
    public List<PermissionResponseDto> findAll() throws SQLException {
        return userPermissionDao.findAll();
    }

    @Override
    public List<PermissionResponseDto> findAllByRole(String username) throws SQLException {
        return userPermissionDao.findAllByRole(username);
    }
}
