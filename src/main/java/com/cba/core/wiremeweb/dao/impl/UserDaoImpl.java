package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.ChangePasswordRequestDto;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.UserMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.model.UserType;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
import com.cba.core.wiremeweb.repository.specification.UserSpecification;
import com.cba.core.wiremeweb.util.UserBean;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.cba.core.wiremeweb.util.UserTypeEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao<UserResponseDto, UserRequestDto> {

    private final UserRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserBean userBean;

    @Value("${application.resource.users}")
    private String resource;

    @Override
    @Cacheable("users")
    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entitiesPage.map(UserMapper::toDto);
    }

    @Override
    @Cacheable("users")
    public List<UserResponseDto> findAll() throws Exception {
        List<User> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entityList
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<User> spec = UserSpecification.userNameLike(searchParamList.get(0).get("userName"));
        Page<User> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entitiesPage.map(UserMapper::toDto);
    }

    @Override
    public UserResponseDto findById(int id) throws Exception {
        User entity = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto deleteById(int id) throws Exception {
        try {
            User entity = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
            UserResponseDto responseDto = UserMapper.toDto(entity);

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
    @CacheEvict(value = "users", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            String remoteAdr = userBean.getRemoteAdr();

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
                                    remoteAdr));
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
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto updateById(int id, UserRequestDto requestDto) throws Exception {

        String remoteAdr = userBean.getRemoteAdr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        User toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getUserName().equals(requestDto.getUserName())) {
            updateRequired = true;
            oldDataMap.put("userName", toBeUpdated.getUserName());
            newDataMap.put("userName", requestDto.getUserName());

            toBeUpdated.setUserName(requestDto.getUserName());
        }
        if (!toBeUpdated.getContactNo().equals(requestDto.getContactNo())) {
            updateRequired = true;
            oldDataMap.put("contactNo", toBeUpdated.getContactNo());
            newDataMap.put("contactNo", requestDto.getContactNo());

            toBeUpdated.setContactNo(requestDto.getContactNo());
        }
        if (!toBeUpdated.getEmail().equals(requestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", requestDto.getEmail());

            toBeUpdated.setEmail(requestDto.getEmail());
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
                    remoteAdr));

            return UserMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto create(UserRequestDto requestDto) throws Exception {
        String remoteAdr = userBean.getRemoteAdr();

        User toInsert = UserMapper.toModel(requestDto);

        User savedEntity = repository.save(toInsert);
        UserResponseDto responseDto = UserMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                remoteAdr));

        return responseDto;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public List<UserResponseDto> createBulk(List<UserRequestDto> requestDtoList) throws Exception {

        String remoteAdr = userBean.getRemoteAdr();

        List<User> entityList = requestDtoList
                .stream()
                .map(UserMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    UserResponseDto responseDto = UserMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(responseDto),
                                remoteAdr));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return responseDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String changePassword(ChangePasswordRequestDto requestDto, String userName) throws Exception {
        try {
            Map<String, Object> map = new HashMap<>();

            UserType userType = new UserType();
            userType.setId(UserTypeEnum.WEB.getValue());

            User entity = repository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new NotFoundException("User not found"));
//            UserResponseDto responseDto = UserMapper.toDto(entity);

            entity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
            repository.saveAndFlush(entity);

            map.put("password", "xxxxxxxx");
            String maskValue = objectMapper.writeValueAsString(map);

            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    entity.getId(), maskValue, maskValue,
                    userBean.getRemoteAdr()));

            return "success";

        } catch (Exception rr) {
            throw rr;
        }
    }
}
