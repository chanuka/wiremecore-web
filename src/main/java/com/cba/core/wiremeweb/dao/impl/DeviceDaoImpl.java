package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceDao;
import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.exception.RecordInUseException;
import com.cba.core.wiremeweb.mapper.DeviceMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
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
public class DeviceDaoImpl implements DeviceDao {

    private final DeviceRepository deviceRepository;
    private final GlobalAuditEntryRepository globalAuditEntryRepository;
    private final HttpServletRequest request;
    @Value("${application.resource.devices}")
    private String resource;


    @Override
    @Cacheable("devices")
    public Page<DeviceResponseDto> findAll(int page, int pageSize) throws Exception {
        Pageable pageable = PageRequest.of(page, pageSize);
        Thread.sleep(2000);

        Page<Device> DevicesPage = deviceRepository.findAll(pageable);
        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);
    }

    @Override
    @Cacheable("devices")
    public List<DeviceResponseDto> findAll() throws Exception {
        List<Device> devicesList = deviceRepository.findAll();
        if (devicesList.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return devicesList
                .stream()
                .map(DeviceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<DeviceResponseDto> findBySerialNoLike(String serialNumber, int page, int pageSize) throws Exception {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Device> DevicesPage = deviceRepository.findBySerialNoLike("%" + serialNumber + "%", pageable);
        if (DevicesPage.isEmpty()) {
            throw new NotFoundException("No Devices found");
        }
        return DevicesPage.map(DeviceMapper::toDto);

    }

    @Override
    public DeviceResponseDto findById(int id) throws Exception {

        Device device = deviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
        return DeviceMapper.toDto(device);
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto deleteById(int id) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Device device = deviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));
            DeviceResponseDto deviceResponseDto = DeviceMapper.toDto(device);

            deviceRepository.deleteById(id);
            globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                    id, objectMapper.writeValueAsString(deviceResponseDto), null,
                    request.getRemoteAddr()));

            return deviceResponseDto;

        } catch (NotFoundException nf) {
            throw nf;
        } catch (DataIntegrityViolationException e) {
            throw new RecordInUseException("Device is in use");
        } catch (Exception rr) {
            throw rr;
        }
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public void deleteByIdList(List<Integer> deviceList) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String remoteAdr = request.getRemoteAddr();

            deviceList.stream()
                    .map((id) -> deviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Device not found")))
                    .collect(Collectors.toList());

            deviceRepository.deleteAllByIdInBatch(deviceList);

            deviceList.stream()
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
            throw new RecordInUseException("Device is in use");
        } catch (Exception ee) {
            ee.printStackTrace();
            throw ee;
        }
    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public DeviceResponseDto updateById(int id, DeviceRequestDto deviceRequestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();
        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        Device toBeUpdated = deviceRepository.findById(id).orElseThrow(() -> new NotFoundException("Device not found"));

        if (!toBeUpdated.getDeviceType().equals(deviceRequestDto.getDeviceType())) {
            updateRequired = true;
            oldDataMap.put("deviceType", toBeUpdated.getDeviceType());
            newDataMap.put("deviceType", deviceRequestDto.getDeviceType());

            toBeUpdated.setDeviceType(deviceRequestDto.getDeviceType());
        }
        if (!toBeUpdated.getEmiNo().equals(deviceRequestDto.getEmiNo())) {
            updateRequired = true;
            oldDataMap.put("emiNo", toBeUpdated.getEmiNo());
            newDataMap.put("emiNo", deviceRequestDto.getEmiNo());

            toBeUpdated.setEmiNo(deviceRequestDto.getEmiNo());
        }
        if (!toBeUpdated.getSerialNo().equals(deviceRequestDto.getSerialNo())) {
            updateRequired = true;
            oldDataMap.put("serialNo", toBeUpdated.getSerialNo());
            newDataMap.put("serialNo", deviceRequestDto.getSerialNo());

            toBeUpdated.setSerialNo(deviceRequestDto.getSerialNo());
        }
        if (!toBeUpdated.getStatus().getStatusCode().equals((deviceRequestDto.isActive()) ? "ACTV" : "DACT")) {
            updateRequired = true;
            oldDataMap.put("active", (toBeUpdated.getStatus().getStatusCode().equals("ACTV")) ? true : false);
            newDataMap.put("active", deviceRequestDto.isActive());

            toBeUpdated.setStatus(new Status((deviceRequestDto.isActive()) ? "ACTV" : "DACT"));
        }
        if (updateRequired) {

            deviceRepository.saveAndFlush(toBeUpdated);
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
    public DeviceResponseDto create(DeviceRequestDto deviceRequestDto) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();


        Device deviceToInsert = DeviceMapper.toModel(deviceRequestDto);
        deviceToInsert.setStatus(new Status(deviceRequestDto.isActive() ? "ACTV" : "DACT"));
        Device savedDevice = deviceRepository.save(deviceToInsert);
        DeviceResponseDto deviceResponseDto = DeviceMapper.toDto(savedDevice);
        globalAuditEntryRepository.save(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedDevice.getId(), null, objectMapper.writeValueAsString(deviceResponseDto),
                remoteAdr));

        return deviceResponseDto;

    }

    @Override
    @CacheEvict(value = "devices", allEntries = true)
    public List<DeviceResponseDto> createBulk(List<DeviceRequestDto> deviceRequestDtoList) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String remoteAdr = request.getRemoteAddr();

        List<Device> deviceList = deviceRequestDtoList
                .stream()
                .map(DeviceMapper::toModel)
                .collect(Collectors.toList());

        return deviceRepository.saveAll(deviceList)
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
