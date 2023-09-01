package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.RoleMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.RoleRepository;
import com.cba.core.wiremeweb.repository.specification.RoleSpecification;
import com.cba.core.wiremeweb.util.UserBean;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class RoleDaoImpl implements GenericDao<RoleResponseDto, RoleRequestDto> {

    private final RoleRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBean userBean;

    @Value("${application.resource.roles}")
    private String resource;

    @Override
    @Cacheable("roles")
    public Page<RoleResponseDto> findAll(int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Role> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return entitiesPage.map(RoleMapper::toDto);
    }

    @Override
    @Cacheable("roles")
    public List<RoleResponseDto> findAll() throws Exception {
        List<Role> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return entityList
                .stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Role> spec = RoleSpecification.roleNameLike(searchParamList.get(0).get("roleName"));
        Page<Role> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return entitiesPage.map(RoleMapper::toDto);
    }

    @Override
    public RoleResponseDto findById(int id) throws Exception {
        Role entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
        return RoleMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto deleteById(int id) throws Exception {
        try {
            Role entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
            RoleResponseDto responseDto = RoleMapper.toDto(entity);

            repository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(responseDto), null,
                    userBean.getRemoteAdr()));

            return responseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {

            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Role not found")))
                    .collect(Collectors.toList());

            repository.deleteAllByIdInBatch(idList);

            idList.stream()
                    .forEach(item -> {
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        objectNode.put("id", item);
                        try {
                            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                    item, objectMapper.writeValueAsString(objectNode), null,
                                    userBean.getRemoteAdr()));
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
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto updateById(int id, RoleRequestDto requestDto) throws Exception {

        Role toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getRoleName().equals(requestDto.getRoleName())) {
            updateRequired = true;
            oldDataMap.put("roleName", toBeUpdated.getRoleName());
            newDataMap.put("roleName", requestDto.getRoleName());

            toBeUpdated.setRoleName(requestDto.getRoleName());
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
                    userBean.getRemoteAdr()));

            return RoleMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto create(RoleRequestDto requestDto) throws Exception {

        Role toInsert = RoleMapper.toModel(requestDto);

        Role savedEntity = repository.save(toInsert);
        RoleResponseDto responseDto = RoleMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBean.getRemoteAdr()));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public List<RoleResponseDto> createBulk(List<RoleRequestDto> requestDtoList) throws Exception {

        List<Role> entityList = requestDtoList
                .stream()
                .map(RoleMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    RoleResponseDto responseDto = RoleMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                userBean.getRemoteAdr()));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }
}
