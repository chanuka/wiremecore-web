package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.PermissionDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Permission;
import com.cba.core.wiremeweb.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PermissionDaoImpl implements PermissionDao<Permission> {

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
    public List<Permission> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        return null;
    }

    @Override
    public Permission findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
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
