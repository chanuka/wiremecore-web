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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
@Transactional
@RequiredArgsConstructor
public class PermissionDaoImpl implements PermissionDao<PermissionResponseDto, PermissionRequestDto> {

    private final PermissionRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.permissions}")
    private String resource;

    @Override
    @Cacheable("permissions")
    public Page<PermissionResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Permission> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Permissions found");
        }
        return entitiesPage.map(PermissionMapper::toDto);
    }

    @Override
    @Cacheable("permissions")
    public List<PermissionResponseDto> findAll() throws Exception {
        List<Permission> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Permissions found");
        }
        return entityList
                .stream()
                .map(PermissionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<PermissionResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public Page<PermissionResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public PermissionResponseDto findById(int id) throws Exception {
        Permission entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
        return PermissionMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public PermissionResponseDto deleteById(int id) throws Exception {
        try {
            Permission entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));
            PermissionResponseDto responseDto = PermissionMapper.toDto(entity);

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
    @CacheEvict(value = "permissions", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {

            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found")))
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
    @CacheEvict(value = "permissions", allEntries = true)
    public PermissionResponseDto updateById(int id, PermissionRequestDto requestDto) throws Exception {

        Permission toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Permission not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (toBeUpdated.getRole().getId() != requestDto.getRoleId()) {
            updateRequired = true;
            oldDataMap.put("roleId", toBeUpdated.getRole().getId());
            newDataMap.put("roleId", requestDto.getRoleId());

            toBeUpdated.setRole(new Role(requestDto.getRoleId()));
        }
        if (toBeUpdated.getResource().getId() != requestDto.getResourceId()) {
            updateRequired = true;
            oldDataMap.put("resourceId", toBeUpdated.getResource().getId());
            newDataMap.put("resourceId", requestDto.getResourceId());

            toBeUpdated.setResource(new Resource(requestDto.getResourceId()));
        }
        if (toBeUpdated.getReadd().intValue() != requestDto.getReadd()) {
            updateRequired = true;
            oldDataMap.put("readd", toBeUpdated.getReadd().intValue());
            newDataMap.put("readd", requestDto.getReadd());

            toBeUpdated.setReadd(requestDto.getReadd().byteValue());
        }
        if (toBeUpdated.getCreated().intValue() != requestDto.getCreated()) {
            updateRequired = true;
            oldDataMap.put("created", toBeUpdated.getCreated().intValue());
            newDataMap.put("created", requestDto.getCreated());

            toBeUpdated.setCreated(requestDto.getCreated().byteValue());
        }
        if (toBeUpdated.getUpdated().intValue() != requestDto.getUpdated()) {
            updateRequired = true;
            oldDataMap.put("updated", toBeUpdated.getUpdated().intValue());
            newDataMap.put("updated", requestDto.getUpdated());

            toBeUpdated.setUpdated(requestDto.getUpdated().byteValue());
        }
        if (toBeUpdated.getDeleted().intValue() != requestDto.getDeleted()) {
            updateRequired = true;
            oldDataMap.put("deleted", toBeUpdated.getDeleted().intValue());
            newDataMap.put("deleted", requestDto.getDeleted());

            toBeUpdated.setDeleted(requestDto.getDeleted().byteValue());
        }

        if (updateRequired) {

            repository.saveAndFlush(toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return PermissionMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public PermissionResponseDto create(PermissionRequestDto requestDto) throws Exception {

        Permission toInsert = PermissionMapper.toModel(requestDto);

        Permission savedEntity = repository.save(toInsert);
        PermissionResponseDto responseDto = PermissionMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "permissions", allEntries = true)
    public List<PermissionResponseDto> createBulk(List<PermissionRequestDto> requestDtoList) throws Exception {

        List<Permission> entityList = requestDtoList
                .stream()
                .map(PermissionMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    PermissionResponseDto responseDto = PermissionMapper.toDto(item);
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
//    @Cacheable("permissions")
    public List<PermissionResponseDto> findAllPermissionsByUser(String username) throws SQLException {
        Iterable<Permission> irt = repository.findAllPermissionsByUser(username);
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(PermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<PermissionResponseDto> findAllPermissionsByUser() throws SQLException {
        Iterable<Permission> irt = repository.findAllPermissionsByUser(userBeanUtil.getUsername());
        List<PermissionResponseDto> result =
                StreamSupport.stream(irt.spliterator(), false)
                        .map(PermissionMapper::toDto)
                        .collect(Collectors.toList());
        return result;
    }
}
