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

import java.util.Date;
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
    private final UserBeanUtil userBeanUtil;
    private final EmailService emailService;

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
    public Page<UserResponseDto> findBySearchParamLike(Map<String, String> searchParamList, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<User> spec = UserSpecification.userNameLike(searchParamList.get("userName"),
                searchParamList.get("name"));
        Page<User> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return entitiesPage.map(UserMapper::toDto);
    }

    @Override
    public Page<UserResponseDto> findBySearchParamLikeByKeyWord(Map<String, String> searchParameter, int page, int pageSize) throws Exception {
        return null;
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
                    userBeanUtil.getRemoteAdr()));

            return responseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
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
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto updateById(int id, UserRequestDto requestDto) throws Exception {

        User toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

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


        if ((toBeUpdated.getDevice() != null ? toBeUpdated.getDevice().getId() : null) != requestDto.getDeviceId()) {
            updateRequired = true;
            oldDataMap.put("deviceId", (toBeUpdated.getDevice() != null ? toBeUpdated.getDevice().getId() : null));
            newDataMap.put("deviceId", requestDto.getDeviceId());
            if (requestDto.getDeviceId() != null) {
                toBeUpdated.setDevice(new Device(requestDto.getDeviceId()));
            } else {
                toBeUpdated.setDevice(null);
            }
        }
        if ((toBeUpdated.getMerchant() != null ? toBeUpdated.getMerchant().getId() : null) != requestDto.getMerchantId()) {
            updateRequired = true;
            oldDataMap.put("merchantId", (toBeUpdated.getMerchant() != null ? toBeUpdated.getMerchant().getId() : null));
            newDataMap.put("merchantId", requestDto.getMerchantId());
            if (requestDto.getMerchantId() != null) {
                toBeUpdated.setMerchant(new Merchant(requestDto.getMerchantId()));
            } else {
                toBeUpdated.setMerchant(null);
            }
        }
        if ((toBeUpdated.getMerchantCustomer() != null ? toBeUpdated.getMerchantCustomer().getId() : null) != requestDto.getPartnerId()) {
            updateRequired = true;
            oldDataMap.put("partnerId", (toBeUpdated.getMerchantCustomer() != null ? toBeUpdated.getMerchantCustomer().getId() : null));
            newDataMap.put("partnerId", requestDto.getPartnerId());
            if (requestDto.getPartnerId() != null) {
                toBeUpdated.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
            } else {
                toBeUpdated.setMerchantCustomer(null);
            }
        }
        if (updateRequired) {

            repository.save(toBeUpdated);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    id, objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return UserMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto create(UserRequestDto requestDto) throws Exception {

        char[] pwd = UserPasswordUtil.generateCommonLangPassword().toCharArray();
        User toInsert = UserMapper.toModel(requestDto);
        toInsert.setPassword(new BCryptPasswordEncoder().encode(pwd.toString()));

        User savedEntity = repository.save(toInsert);
        UserResponseDto responseDto = UserMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        String message = "Your Password for Wireme is : " + pwd.toString();
        emailService.sendEmail(toInsert.getEmail(), message);

        return responseDto;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public List<UserResponseDto> createBulk(List<UserRequestDto> requestDtoList) throws Exception {

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
                                userBeanUtil.getRemoteAdr()));
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
            userType.setId(DeviceTypeEnum.WEB.getValue());

            User entity = repository.findByUserNameAndUserType(userName, userType).orElseThrow(() -> new NotFoundException("User not found"));

            if (passwordEncoder.matches(requestDto.getCurrentPassword(), entity.getPassword())) {
                entity.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
                repository.saveAndFlush(entity);
                map.put("password", "xxxxxxxx");
                String maskValue = objectMapper.writeValueAsString(map);

                globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                        entity.getId(), maskValue, maskValue,
                        userBeanUtil.getRemoteAdr()));
            } else {
                throw new NotFoundException("Fail - Old Password mismatch");
            }

            return "success";

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    public String accountLockReset(String userName) throws Exception {
        User toBeUpdated = repository.findByUserName(userName).orElseThrow(() -> new NotFoundException("User not found"));
        Map<String, Object> oldValueMap = new HashMap<>();
        Map<String, Object> newValueMap = new HashMap<>();
        oldValueMap.put("loginAttempt", toBeUpdated.getLoginAttempt());
        oldValueMap.put("status", toBeUpdated.getStatus().getStatusCode());

        toBeUpdated.setLoginAttempt(0);
        toBeUpdated.setStatus(new Status("ACTV"));
        newValueMap.put("loginAttempt", toBeUpdated.getLoginAttempt());
        newValueMap.put("status", toBeUpdated.getStatus().getStatusCode());

        /*
        Old and new values are not set to JSON
         */
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                toBeUpdated.getId(), objectMapper.writeValueAsString(oldValueMap), objectMapper.writeValueAsString(newValueMap),
                userBeanUtil.getRemoteAdr()));
        return null;
    }
}
