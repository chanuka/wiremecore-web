package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.PermissionDao;
import com.cba.core.wiremeweb.dto.PermissionRequestDto;
import com.cba.core.wiremeweb.dto.PermissionResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.PermissionMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Permission;
import com.cba.core.wiremeweb.model.Resource;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.PermissionRepository;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
@RequiredArgsConstructor
public class PermissionDaoImpl implements PermissionDao<Permission, Permission> {

    private final PermissionRepository repository;

    @Override
    @Cacheable("permissions")
    public Page<Permission> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("permissions")
    public List<Permission> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Permission> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<Permission> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Permission findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public Permission deleteById(int id) throws Exception {
        repository.deleteById(id);
        return new Permission();

    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public Permission updateById(int id, Permission toBeUpdated) throws Exception {
        return repository.saveAndFlush(toBeUpdated);
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public Permission create(Permission toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public List<Permission> createBulk(List<Permission> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
//    @Cacheable("permissions")
    public Iterable<Permission> findAllPermissionsByUser(String username) throws SQLException {
        return repository.findAllPermissionsByUser(username);
    }

}
