package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.specification.DeviceSpecification;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class DeviceDaoImpl implements GenericDao<DeviceResponseDto, DeviceRequestDto> {

    private final DeviceRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.devices}")
    private String resource;

    @Override
    @Cacheable("devices")
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Device> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceMapper::toDto);
    }

    @Override
    @Cacheable("devices")
    public List<DeviceResponseDto> findAll() throws Exception {
        List<Device> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Device> spec = DeviceSpecification.serialNoLikeAndDeviceTypeLike(
                searchParamList.get(0).get("serialNumber"),
                searchParamList.get(0).get("deviceType"),
                searchParamList.get(0).get("status"),
                searchParamList.get(0).get("deviceModel"),
                searchParamList.get(0).get("deviceVendor")
                );

        Page<Device> entitiesPage = repository.findAll(spec, pageable);

        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceMapper::toDto);

    }

    @Override
    public DeviceResponseDto findById(int id) throws Exception {

        Device entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
        return DeviceMapper.toDto(entity);
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto deleteById(int id) throws Exception {
        try {
            Device entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
            DeviceResponseDto responseDto = DeviceMapper.toDto(entity);

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
    @CacheEvict(value = "devices", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            idList.stream()
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found")))
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
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto updateById(int id, DeviceRequestDto requestDto) throws Exception {

        Device toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdated.getDeviceType().equals(requestDto.getDeviceType())) {
            updateRequired = true;
            oldDataMap.put("deviceType", toBeUpdated.getDeviceType());
            newDataMap.put("deviceType", requestDto.getDeviceType());

            toBeUpdated.setDeviceType(requestDto.getDeviceType());
        }
        if (!toBeUpdated.getEmiNo().equals(requestDto.getEmiNo())) {
            updateRequired = true;
            oldDataMap.put("emiNo", toBeUpdated.getEmiNo());
            newDataMap.put("emiNo", requestDto.getEmiNo());

            toBeUpdated.setEmiNo(requestDto.getEmiNo());
        }
        if (!toBeUpdated.getSerialNo().equals(requestDto.getSerialNo())) {
            updateRequired = true;
            oldDataMap.put("serialNo", toBeUpdated.getSerialNo());
            newDataMap.put("serialNo", requestDto.getSerialNo());

            toBeUpdated.setSerialNo(requestDto.getSerialNo());
        }
        if ((toBeUpdated.getDeviceModel() != null ? toBeUpdated.getDeviceModel().getId() : null) != requestDto.getModelId()) {
            updateRequired = true;
            oldDataMap.put("modelId", (toBeUpdated.getDeviceModel() != null ? toBeUpdated.getDeviceModel().getId() : null));
            newDataMap.put("modelId", requestDto.getModelId());

            toBeUpdated.setDeviceModel(new DeviceModel(requestDto.getModelId()));
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

            return DeviceMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }

    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto create(DeviceRequestDto requestDto) throws Exception {

        Device toInsert = DeviceMapper.toModel(requestDto);
        toInsert.setStatus(new Status(requestDto.getStatus()));
        Device savedEntity = repository.save(toInsert);
        DeviceResponseDto responseDto = DeviceMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;

    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> requestDtoList) throws Exception {

        List<Device> entityList = requestDtoList
                .stream()
                .map(DeviceMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    DeviceResponseDto responseDto = DeviceMapper.toDto(item);
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
