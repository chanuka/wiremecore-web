package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.RoleDao;
import com.cba.core.wiremeweb.dto.RoleRequestDto;
import com.cba.core.wiremeweb.dto.RoleResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.mapper.RoleMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Role;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.RoleRepository;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
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
public class RoleDaoImpl implements RoleDao {

    private final RoleRepository roleRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final HttpServletRequest request;
    @Value("${application.resource.roles}")
    private String resource;

    @Override
    @Cacheable("roles")
    public Page<RoleResponseDto> findAll(int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Role> rolesPage = roleRepository.findAll(pageable);
        if (rolesPage.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return rolesPage.map(RoleMapper::toDto);
    }

    @Override
    @Cacheable("roles")
    public List<RoleResponseDto> findAll() throws Exception {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return roleList
                .stream()
                .map(RoleMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<RoleResponseDto> findByRoleNameLike(String roleName, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Role> rolesPage = roleRepository.findByRoleNameLike("%" + roleName + "%", pageable);
        if (rolesPage.isEmpty()) {
            throw new NotFoundException("No Roles found");
        }
        return rolesPage.map(RoleMapper::toDto);
    }

    @Override
    public RoleResponseDto findById(int id) throws Exception {
        Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
        return RoleMapper.toDto(role);
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto deleteById(int id) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Role role = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));
            RoleResponseDto roleResponseDto = RoleMapper.toDto(role);

            roleRepository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(roleResponseDto), null,
                    request.getRemoteAddr()));

            return roleResponseDto;

        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Role is in use");
        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public void deleteByIdList(List<Integer> roleList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String remoteAdr = request.getRemoteAddr();

            roleList.stream()
                    .map((id) -> roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found")))
                    .collect(Collectors.toList());

            roleRepository.deleteAllByIdInBatch(roleList);

            roleList.stream()
                    .forEach(item -> {
                        ObjectNode objectNode = objectMapper.createObjectNode();
                        objectNode.put("id", item);
                        try {
                            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                                    item, objectMapper.writeValueAsString(objectNode), null,
                                    remoteAdr));
                        } catch (Exception e) {
                            throw new RuntimeException("Exception occurred for Auditing: ");// only unchecked exception can be passed
                        }
                    });
        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("User is in use");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto updateById(int id, RoleRequestDto roleRequestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        Role toBeUpdated = roleRepository.findById(id).orElseThrow(() -> new NotFoundException("Role not found"));

        if (!toBeUpdated.getRoleName().equals(roleRequestDto.getRoleName())) {
            updateRequired = true;
            oldDataMap.put("roleName", toBeUpdated.getRoleName());
            newDataMap.put("roleName", roleRequestDto.getRoleName());

            toBeUpdated.setRoleName(roleRequestDto.getRoleName());
        }
        if (!toBeUpdated.getStatus().equals(roleRequestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", roleRequestDto.getStatus());

            toBeUpdated.setStatus(new Status(roleRequestDto.getStatus()));
        }
        if (updateRequired) {

            roleRepository.saveAndFlush(toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    remoteAdr));

            return RoleMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public RoleResponseDto create(RoleRequestDto roleRequestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();


        Role roleToInsert = RoleMapper.toModel(roleRequestDto);

        Role savedRole = roleRepository.save(roleToInsert);
        RoleResponseDto roleResponseDto = RoleMapper.toDto(savedRole);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedRole.getId(), null, objectMapper.writeValueAsString(roleResponseDto),
                remoteAdr));

        return roleResponseDto;
    }

    @Override
    @CacheEvict(value = "roles", allEntries = true)
    public List<RoleResponseDto> createBulk(List<RoleRequestDto> roleRequestDtoList) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        List<Role> roleList = roleRequestDtoList
                .stream()
                .map(RoleMapper::toModel)
                .collect(Collectors.toList());

        return roleRepository.saveAll(roleList)
                .stream()
                .map(item -> {
                    RoleResponseDto roleResponseDto = RoleMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(roleResponseDto),
                                remoteAdr));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return roleResponseDto;
                })
                .collect(Collectors.toList());
    }
}
