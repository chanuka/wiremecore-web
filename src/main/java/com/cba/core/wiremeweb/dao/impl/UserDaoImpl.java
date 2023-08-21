package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.UserDao;
import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.mapper.UserMapper;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.User;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.UserRepository;
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
public class UserDaoImpl implements UserDao {

    private final UserRepository userRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final HttpServletRequest request;
    @Value("${application.resource.users}")
    private String resource;

    @Override
    @Cacheable("users")
    public Page<UserResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<User> usersPage = userRepository.findAll(pageable);
        if (usersPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return usersPage.map(UserMapper::toDto);
    }

    @Override
    @Cacheable("users")
    public List<UserResponseDto> findAll() throws Exception {
        List<User> userList = userRepository.findAll();
        if (userList.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return userList
                .stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<UserResponseDto> findByUserNameLike(String userName, int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<User> usersPage = userRepository.findByUserNameLike("%" + userName + "%", pageable);
        if (usersPage.isEmpty()) {
            throw new NotFoundException("No Users found");
        }
        return usersPage.map(UserMapper::toDto);
    }

    @Override
    public UserResponseDto findById(int id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapper.toDto(user);
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto deleteById(int id) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
            UserResponseDto userResponseDto = UserMapper.toDto(user);

            userRepository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(userResponseDto), null,
                    request.getRemoteAddr()));

            return userResponseDto;

        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("User is in use");
        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public void deleteByIdList(List<Integer> userList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String remoteAdr = request.getRemoteAddr();

            userList.stream()
                    .map((id) -> userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found")))
                    .collect(Collectors.toList());

            userRepository.deleteAllByIdInBatch(userList);

            userList.stream()
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
    @CacheEvict(value = "users", allEntries = true)
    public UserResponseDto updateById(int id, UserRequestDto userRequestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        User toBeUpdated = userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));

        if (!toBeUpdated.getName().equals(userRequestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", userRequestDto.getName());

            toBeUpdated.setName(userRequestDto.getName());
        }
        if (!toBeUpdated.getUserName().equals(userRequestDto.getUserName())) {
            updateRequired = true;
            oldDataMap.put("userName", toBeUpdated.getUserName());
            newDataMap.put("userName", userRequestDto.getUserName());

            toBeUpdated.setUserName(userRequestDto.getUserName());
        }
        if (!toBeUpdated.getContactNo().equals(userRequestDto.getContactNo())) {
            updateRequired = true;
            oldDataMap.put("contactNo", toBeUpdated.getContactNo());
            newDataMap.put("contactNo", userRequestDto.getContactNo());

            toBeUpdated.setContactNo(userRequestDto.getContactNo());
        }
        if (!toBeUpdated.getEmail().equals(userRequestDto.getEmail())) {
            updateRequired = true;
            oldDataMap.put("email", toBeUpdated.getEmail());
            newDataMap.put("email", userRequestDto.getEmail());

            toBeUpdated.setEmail(userRequestDto.getEmail());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals(userRequestDto.getStatus())) {
            updateRequired = true;
            oldDataMap.put("status", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("status", userRequestDto.getStatus());

            toBeUpdated.setStatus(new Status(userRequestDto.getStatus()));
        }
        if (updateRequired) {

            userRepository.saveAndFlush(toBeUpdated);
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
    public UserResponseDto create(UserRequestDto userRequestDto) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();


        User userToInsert = UserMapper.toModel(userRequestDto);

        User savedUser = userRepository.save(userToInsert);
        UserResponseDto userResponseDto = UserMapper.toDto(savedUser);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedUser.getId(), null, objectMapper.writeValueAsString(userResponseDto),
                remoteAdr));

        return userResponseDto;
    }

    @Override
    @CacheEvict(value = "users", allEntries = true)
    public List<UserResponseDto> createBulk(List<UserRequestDto> userRequestDtoList) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        List<User> userList = userRequestDtoList
                .stream()
                .map(UserMapper::toModel)
                .collect(Collectors.toList());

        return userRepository.saveAll(userList)
                .stream()
                .map(item -> {
                    UserResponseDto userResponseDto = UserMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(userResponseDto),
                                remoteAdr));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return userResponseDto;
                })
                .collect(Collectors.toList());
    }
}
