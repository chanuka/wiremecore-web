package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.UserRoleRequestDto;
import com.cba.core.wiremeweb.dto.UserRoleResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.UserRoleMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.UserRoleRepository;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class UserRoleDaoImpl implements GenericDao<UserRoleResponseDto, UserRoleRequestDto> {

    private final UserRoleRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.userroles}")
    private String resource;

    @Override
    @Cacheable("userroles")
    public Page<UserRoleResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<UserRole> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No User Roles found");
        }
        return entitiesPage.map(UserRoleMapper::toDto);
    }

    @Override
    @Cacheable("userroles")
    public List<UserRoleResponseDto> findAll() throws Exception {
        List<UserRole> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No User Roles found");
        }
        return entityList
                .stream()
                .map(UserRoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserRoleResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public UserRoleResponseDto findById(int id) throws Exception {
        UserRole entity = repository.findById(id).orElseThrow(() -> new NotFoundException("User Role not found"));
        return UserRoleMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "userroles", allEntries = true)
    public UserRoleResponseDto deleteById(int id) throws Exception {
        try {
            UserRole entity = repository.findById(id).orElseThrow(() -> new NotFoundException("User Role not found"));
            UserRoleResponseDto responseDto = UserRoleMapper.toDto(entity);

            repository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(responseDto), null,
                    userBeanUtil.getRemoteAdr()));

            return responseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "userroles", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {

            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("User not found")))
                    .collect(Collectors.toList());

            repository.deleteAllByIdInBatch(idList);

            idList.stream()
                    .forEach(item -> {
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        objectNode.put("id", item);
                        try {
                            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                    item, objectMapper.writeValueAsString(objectNode), null,
                                    userBeanUtil.getRemoteAdr()));
                        } catch (Exception e) {
                            throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                        }
                    });
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    @CacheEvict(value = "userroles", allEntries = true)
    public UserRoleResponseDto updateById(int id, UserRoleRequestDto requestDto) throws Exception {

        UserRole toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("User Role not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (toBeUpdated.getUserByUserId().getId() != requestDto.getUserId()) {
            updateRequired = true;
            oldDataMap.put("userId", toBeUpdated.getUserByUserId().getId());
            newDataMap.put("userId", requestDto.getUserId());

            toBeUpdated.setUserByUserId(new User(requestDto.getUserId()));
        }
        if (toBeUpdated.getRole().getId() != requestDto.getRoleId()) {
            updateRequired = true;
            oldDataMap.put("roleId", toBeUpdated.getRole().getId());
            newDataMap.put("roleId", requestDto.getRoleId());

            toBeUpdated.setRole(new Role(requestDto.getRoleId()));
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(requestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", requestDto.getStatus());

            toBeUpdated.setStatus(new Status(requestDto.getStatus()));
        }
        if (updateRequired) {

            repository.saveAndFlush(toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return UserRoleMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "userroles", allEntries = true)
    public UserRoleResponseDto create(UserRoleRequestDto requestDto) throws Exception {

        UserRole toInsert = UserRoleMapper.toModel(requestDto);

        UserRole savedEntity = repository.save(toInsert);
        UserRoleResponseDto responseDto = UserRoleMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "userroles", allEntries = true)
    public List<UserRoleResponseDto> createBulk(List<UserRoleRequestDto> requestDtoList) throws Exception {

        List<UserRole> entityList = requestDtoList
                .stream()
                .map(UserRoleMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    UserRoleResponseDto responseDto = UserRoleMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                userBeanUtil.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }
}
