package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.UserRole;
import com.cba.core.wiremeweb.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class UserRoleDaoImpl implements GenericDao<UserRole> {

    private final UserRoleRepository repository;

    @Override
//    @Cacheable("userroles")
    public Page<UserRole> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
//    @Cacheable("userroles")
    public List<UserRole> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<UserRole> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public List<UserRole> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter) throws Exception {
        return null;
    }

    @Override
    public UserRole findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User Role not found"));
    }

    @Override
//    @CacheEvict(value = "userroles", allEntries = true)
    public void deleteById(int id) throws Exception {
        repository.deleteById(id);
    }

    @Override
//    @CacheEvict(value = "userroles", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
//    @CacheEvict(value = "userroles", allEntries = true)
    public UserRole updateById(int id, UserRole toBeUpdated) throws Exception {
        return repository.saveAndFlush(toBeUpdated);
    }

    @Override
//    @CacheEvict(value = "userroles", allEntries = true)
    public UserRole create(UserRole toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
//    @CacheEvict(value = "userroles", allEntries = true)
    public List<UserRole> createBulk(List<UserRole> entityList) throws Exception {
        return repository.saveAll(entityList);
    }
}
