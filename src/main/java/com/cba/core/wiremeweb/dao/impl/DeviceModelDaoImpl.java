package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceModelMapper;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceModelRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class DeviceModelDaoImpl implements GenericDao<DeviceModelResponseDto, DeviceModelRequestDto> {

    private final DeviceModelRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.device_model}")
    private String resource;

    @Override
    public Page<DeviceModelResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<DeviceModel> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceModelMapper::toDto);
    }

    @Override
    public List<DeviceModelResponseDto> findAll() throws Exception {
        List<DeviceModel> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceModelMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceModelResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public DeviceModelResponseDto findById(int id) throws Exception {
        DeviceModel entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Model not found"));
        return DeviceModelMapper.toDto(entity);
    }

    @Override
    public DeviceModelResponseDto deleteById(int id) throws Exception {
        try {
            DeviceModel entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Model not found"));
            DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(entity);

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
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Device Model not found")))
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
    public DeviceModelResponseDto updateById(int id, DeviceModelRequestDto requestDto) throws Exception {
        DeviceModel toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Model not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getName().equals(requestDto.getName())) {
            updateRequired = true;
            oldDataMap.put("name", toBeUpdated.getName());
            newDataMap.put("name", requestDto.getName());

            toBeUpdated.setName(requestDto.getName());
        }
        if (!toBeUpdated.getImg().equals(requestDto.getImg())) {
            updateRequired = true;
            oldDataMap.put("img", toBeUpdated.getImg());
            newDataMap.put("img", requestDto.getImg());

            toBeUpdated.setImg(requestDto.getImg());
        }
        if (toBeUpdated.getDeviceVendor().getId() != requestDto.getDeviceVendor()) {
            updateRequired = true;
            oldDataMap.put("deviceVendor", toBeUpdated.getStatus().getStatusCode());
            newDataMap.put("deviceVendor", requestDto.getStatus());

            toBeUpdated.setDeviceVendor(new DeviceVendor(requestDto.getDeviceVendor()));
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

            return DeviceModelMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public DeviceModelResponseDto create(DeviceModelRequestDto requestDto) throws Exception {
        DeviceModel toInsert = DeviceModelMapper.toModel(requestDto);
        toInsert.setStatus(new Status(requestDto.getStatus()));
        DeviceModel savedEntity = repository.save(toInsert);
        DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<DeviceModelResponseDto> createBulk(List<DeviceModelRequestDto> requestDtoList) throws Exception {
        List<DeviceModel> entityList = requestDtoList
                .stream()
                .map(DeviceModelMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    DeviceModelResponseDto responseDto = DeviceModelMapper.toDto(item);
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
