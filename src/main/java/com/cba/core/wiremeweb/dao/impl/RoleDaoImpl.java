package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.repository.RoleRepository;
import com.cba.core.wiremeweb.repository.specification.RoleSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class RoleDaoImpl implements GenericDao<Role, Role> {

    private final RoleRepository repository;


    @Override
    @Cacheable("roles")
    public Page<Role> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("roles")
    public List<Role> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<Role> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Role> spec = RoleSpecification.roleNameLike(searchParamList.get("roleName"));
        return repository.findAll(spec, pageable);

    }

    @Override
    public Page<Role> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Role findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public Role deleteById(int id) throws Exception {
        repository.deleteById(id);

        return new Role();
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public Role updateById(int id, Role toBeUpdated) throws Exception {
        return repository.saveAndFlush(toBeUpdated);
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public Role create(Role toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public List<Role> createBulk(List<Role> entityList) throws Exception {
        return repository.saveAll(entityList);
    }
}
