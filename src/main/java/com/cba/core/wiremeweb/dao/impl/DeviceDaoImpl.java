package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GenericDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.repository.specification.DeviceSpecification;
import com.cba.core.wiremeweb.util.UserOperationEnum;
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
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class DeviceDaoImpl implements GenericDao<DeviceResponseDto,DeviceRequestDto> {

    private final DeviceRepository repository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final HttpServletRequest request;
    @Value("${application.resource.devices}")
    private String resource;


    @Override
    @Cacheable("devices")
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);

        Page<Device> DevicesPage = repository.findAll(pageable);
        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);
    }

    @Override
    @Cacheable("devices")
    public List<DeviceResponseDto> findAll() throws Exception {
        List<Device> devicesList = repository.findAll();
        if (devicesList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return devicesList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceResponseDto> findBySearchParamLike(List<Map<String, String>> searchParamList, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Specification<Device> spec = DeviceSpecification.serialNoLikeAndDeviceTypeLike(
                searchParamList.get(0).get("serialNumber"),
                searchParamList.get(0).get("deviceType"));

        Page<Device> DevicesPage = repository.findAll(spec,pageable);

        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);

    }

    @Override
    public DeviceResponseDto findById(int id) throws Exception {

        Device device = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
        return DeviceMapper.toDto(device);
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto deleteById(int id) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Device device = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
            DeviceResponseDto deviceResponseDto = DeviceMapper.toDto(device);

            repository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(deviceResponseDto), null,
                    request.getRemoteAddr()));

            return deviceResponseDto;

        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public void deleteByIdList(List<Integer> idList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String remoteAdr = request.getRemoteAddr();

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
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto updateById(int id, DeviceRequestDto requestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        Device toBeUpdated = repository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));

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

            return DeviceMapper.toDto(toBeUpdated);

        } else {
            throw new NotFoundException("No Changes found");
        }

    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto create(DeviceRequestDto requestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();


        Device deviceToInsert = DeviceMapper.toModel(requestDto);
        deviceToInsert.setStatus(new Status(requestDto.getStatus()));
        Device savedDevice = repository.save(deviceToInsert);
        DeviceResponseDto deviceResponseDto = DeviceMapper.toDto(savedDevice);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedDevice.getId(), null, objectMapper.writeValueAsString(deviceResponseDto),
                remoteAdr));

        return deviceResponseDto;

    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> requestDtoList) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        List<Device> deviceList = requestDtoList
                .stream()
                .map(DeviceMapper::toModel)
                .collect(Collectors.toList());

        return repository.saveAll(deviceList)
                .stream()
                .map(item -> {
                    DeviceResponseDto deviceResponseDto = DeviceMapper.toDto(item);
                    try {
                        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                                item.getId(), null, objectMapper.writeValueAsString(deviceResponseDto),
                                remoteAdr));
                    } catch (Exception e) {
                        throw new RuntimeException("Exception occurred in Auditing: ");// only unchecked exception can be passed
                    }
                    return deviceResponseDto;
                })
                .collect(Collectors.toList());
    }
}
