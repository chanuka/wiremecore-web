package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.UserPermissionDao;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.mapper.UserPermissionMapper;
import com.cba.core.wiremeweb.model.Permission;
import com.cba.core.wiremeweb.repository.UserPermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Transactional
@RequiredArgsConstructor
public class UserPermissionDaoImpl implements UserPermissionDao {

    private final UserPermissionRepository userPermissionRepository;

    @Override
    public List<PermissionResponseDto> findAll() throws SQLException {
        Iterable<Permission> irt = userPermissionRepository.findAll();
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(UserPermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }

    @Override
//    @Cacheable("permissions")
    public List<PermissionResponseDto> findAllByRole(String username) throws SQLException {
        Iterable<Permission> irt = userPermissionRepository.findAllByRole(username);
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(UserPermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }
}
