package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.UserMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.repository.specification.UserSpecification;
import com.cba.core.wiremeweb.service.EmailService;
import com.cba.core.wiremeweb.util.DeviceTypeEnum;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.cba.core.wiremeweb.util.UserPasswordUtil;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao<User, User> {

    private final UserRepository repository;

    @Override
    @Cacheable("users")
    public Page<User> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        return repository.findAll(pageable);
    }

    @Override
    @Cacheable("users")
    public List<User> findAll() throws Exception {
        return repository.findAll();
    }

    @Override
    public Page<User> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<User> spec = UserSpecification.userNameLike(searchParamList.get("userName"),
                searchParamList.get("name"));
        return repository.findAll(spec, pageable);
    }

    @Override
    public Page<User> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public User findById(int id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User deleteById(int id) throws Exception {
        repository.deleteById(id);

        return new User();
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        repository.deleteAllByIdInBatch(idList);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User updateById(int id, User toBeUpdated) throws Exception {
        return repository.save(toBeUpdated);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public User create(User toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public List<User> createBulk(List<User> entityList) throws Exception {
        return repository.saveAll(entityList);
    }

    @Override
    public User changePassword(User entity) throws Exception {
        return repository.saveAndFlush(entity);
    }

    @Override
    public User findByUserName(String userName) throws Exception {
        return repository.findByUserName(userName).orElseThrow(() -> new NotFoundException("User Not Found"));
    }
}
