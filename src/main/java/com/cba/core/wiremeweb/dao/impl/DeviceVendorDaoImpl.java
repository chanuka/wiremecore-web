package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceVendorRequestDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceModelMapper;
import com.cba.core.wiremeweb.mapper.DeviceVendorMapper;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceModelRepository;
import com.cba.core.wiremeweb.repository.DeviceVendorRepository;
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
public class DeviceVendorDaoImpl implements GenericDao<DeviceVendorResponseDto, DeviceVendorRequestDto> {

    private final DeviceVendorRepository repository;
    private final DeviceModelRepository deviceModelRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final ObjectMapper objectMapper;
    private final UserBeanUtil userBeanUtil;

    @Value("${application.resource.device_vendor}")
    private String resource;

    @Override
    public Page<DeviceVendorResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<DeviceVendor> entitiesPage = repository.findAll(pageable);
        if (entitiesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entitiesPage.map(DeviceVendorMapper::toDto);
    }

    @Override
    public List<DeviceVendorResponseDto> findAll() throws Exception {
        List<DeviceVendor> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return entityList
                .stream()
                .map(DeviceVendorMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceVendorResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {
        return null;
    }

    @Override
    public DeviceVendorResponseDto findById(int id) throws Exception {
        DeviceVendor entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Vendor not found"));
        List<DeviceModel> deviceModels = deviceModelRepository.findAllByDeviceVendor_Id(entity.getId());

        DeviceVendorResponseDto deviceVendorResponseDto = DeviceVendorMapper.toDto(entity);
        deviceVendorResponseDto.setDeviceModels(deviceModels.stream().map(DeviceModelMapper::toDto).collect(Collectors.toList()));
        return deviceVendorResponseDto;
    }

    @Override
    public DeviceVendorResponseDto deleteById(int id) throws Exception {
        try {
            DeviceVendor entity = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Vendor not found"));
            DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(entity);

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
                    .map((id) -> repository.findById(id).orElseThrow(() -> new NotFoundException("Device Vendor not found")))
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
    public DeviceVendorResponseDto updateById(int id, DeviceVendorRequestDto requestDto) throws Exception {
        DeviceVendor toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Device Vendor not found"));

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

            return DeviceVendorMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public DeviceVendorResponseDto create(DeviceVendorRequestDto requestDto) throws Exception {
        DeviceVendor toInsert = DeviceVendorMapper.toModel(requestDto);
        toInsert.setStatus(new Status(requestDto.getStatus()));
        DeviceVendor savedEntity = repository.save(toInsert);
        DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(savedEntity);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public List<DeviceVendorResponseDto> createBulk(List<DeviceVendorRequestDto> requestDtoList) throws Exception {
        List<DeviceVendor> entityList = requestDtoList
                .stream()
                .map(DeviceVendorMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(entityList)
                .stream()
                .map(item -> {
                    DeviceVendorResponseDto responseDto = DeviceVendorMapper.toDto(item);
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
