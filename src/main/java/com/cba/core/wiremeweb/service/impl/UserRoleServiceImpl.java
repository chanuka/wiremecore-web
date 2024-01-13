package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.UserRoleRequestDto;
import com.cba.core.wiremeweb.dto.UserRoleResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.UserRoleMapper;
import com.cba.core.wiremeweb.model.*;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRoleServiceImpl implements GenericService<UserRoleResponseDto, UserRoleRequestDto> {

    private final GenericDao<UserRole, UserRole> dao;
    private final UserBeanUtil userBeanUtil;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.userroles}")
    private String resource;

    @Override
    public Page<UserRoleResponseDto> findAll(int page, int pageSize) throws Exception {
        Page<UserRole> entitiesPage = dao.findAll(page, pageSize);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No User Roles found");
        }
        return entitiesPage.map(UserRoleMapper::toDto);
    }

    @Override
    public List<UserRoleResponseDto> findAll() throws Exception {
        List<UserRole> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No User Roles found");
        }
        return entityList
                .stream()
                .map(UserRoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserRoleResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<UserRoleResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public UserRoleResponseDto findById(int id) throws Exception {
        UserRole entity = dao.findById(id);
        return UserRoleMapper.toDto(entity);
    }

    @Override
    public UserRoleResponseDto deleteById(int id) throws Exception {
        UserRole entity = dao.findById(id);
        UserRoleResponseDto responseDto = UserRoleMapper.toDto(entity);

        dao.deleteById(id);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public void deleteByIdList(List<Integer> idList) throws Exception {
        idList.stream()
                .map((id) -> {
                    try {
                        return dao.findById(id);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return null;
                })
                .collect(Collectors.toList());

        dao.deleteByIdList(idList);

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
    }

    @Override
    public UserRoleResponseDto updateById(int id, UserRoleRequestDto requestDto) throws Exception {
        UserRole toBeUpdated = dao.findById(id);

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

            dao.updateById(id, toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return UserRoleMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public UserRoleResponseDto create(UserRoleRequestDto requestDto) throws Exception {

        UserRole toInsert = UserRoleMapper.toModel(requestDto);

        UserRole savedEntity = dao.create(toInsert);
        UserRoleResponseDto responseDto = UserRoleMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<UserRoleResponseDto> createBulk(List<UserRoleRequestDto> requestDtoList) throws Exception {
        List<UserRole> entityList = requestDtoList
                .stream()
                .map(UserRoleMapper::toModel)
                .collect(Collectors.toList());

        return dao.createBulk(entityList)
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

    @Override
    public byte[] exportPdfReport() throws Exception {
        return new byte[0];
    }

    @Override
    public byte[] exportExcelReport() throws Exception {
        return new byte[0];
    }
}
