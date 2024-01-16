package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.DeviceConfigDao;
import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceConfigMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceConfig;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.service.DeviceConfigService;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class DeviceConfigServiceImpl implements DeviceConfigService {

    private final DeviceConfigDao dao;
    private final GlobalAuditDao globalAuditDao;
    private final UserBeanUtil userBeanUtil;
    private final ObjectMapper objectMapper;

    @Value("${application.resource.deviceconfig}")
    private String resource;

    @Override
    public DeviceConfigResponseDto findById(int id) throws Exception {
        DeviceConfig entity = dao.findById(id);
        DeviceConfigResponseDto responseDto = objectMapper.readValue(entity.getConfig(), DeviceConfigResponseDto.class);
        return DeviceConfigMapper.toDto(responseDto, entity);
    }

    @Override
    public DeviceConfigResponseDto create(DeviceConfigRequestDto requestDto) throws Exception {
        DeviceConfig toInsert = DeviceConfigMapper.toModel(requestDto);
        toInsert.setConfig(objectMapper.writeValueAsString(requestDto));

        DeviceConfig savedEntity = dao.create(toInsert);

        DeviceConfigResponseDto responseDto = objectMapper.readValue(savedEntity.getConfig(), DeviceConfigResponseDto.class);
        responseDto.setId(savedEntity.getId());

        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.CREATE.getValue(),
                savedEntity.getId(), null, objectMapper.writeValueAsString(responseDto),
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }

    @Override
    public DeviceConfigResponseDto update(int id, DeviceConfigRequestDto requestDto) throws Exception {
        DeviceConfig toBeUpdatedEntity = dao.findById(id);
        DeviceConfigResponseDto toBeUpdatedDto = objectMapper.readValue(toBeUpdatedEntity.getConfig(), DeviceConfigResponseDto.class);

        boolean updateRequired = false;
        Map<String, Object> oldDataMap = new HashMap<>();
        Map<String, Object> newDataMap = new HashMap<>();

        if (!toBeUpdatedEntity.getConfig().equals(objectMapper.writeValueAsString(requestDto))) {

            if (!toBeUpdatedEntity.getStatus().getStatusCode().equals(requestDto.getStatus())) {
                updateRequired = true;
                oldDataMap.put("status", toBeUpdatedEntity.getStatus().getStatusCode());
                newDataMap.put("status", requestDto.getStatus());

                toBeUpdatedEntity.setStatus(new Status(requestDto.getStatus()));
                toBeUpdatedDto.setStatus(requestDto.getStatus());
            }
            if (!toBeUpdatedEntity.getConfigType().equals(requestDto.getConfigType())) {
                updateRequired = true;
                oldDataMap.put("configType", toBeUpdatedEntity.getConfigType());
                newDataMap.put("configType", requestDto.getConfigType());

                toBeUpdatedEntity.setConfigType(requestDto.getConfigType());
                toBeUpdatedDto.setConfigType(requestDto.getConfigType());
            }
            if (toBeUpdatedEntity.getDevice().getId() != (requestDto.getDeviceId())) {
                updateRequired = true;
                oldDataMap.put("deviceId", toBeUpdatedEntity.getDevice().getId());
                newDataMap.put("deviceId", requestDto.getDeviceId());

                toBeUpdatedEntity.setDevice(new Device(requestDto.getDeviceId()));
                toBeUpdatedDto.setDeviceId(requestDto.getDeviceId());
            }
        }


        if (updateRequired) {

            toBeUpdatedEntity.setConfig(objectMapper.writeValueAsString(toBeUpdatedDto));
            dao.update(id, toBeUpdatedEntity);
            globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.UPDATE.getValue(),
                    toBeUpdatedEntity.getId(), objectMapper.writeValueAsString(oldDataMap), objectMapper.writeValueAsString(newDataMap),
                    userBeanUtil.getRemoteAdr()));

            return DeviceConfigMapper.toDto(toBeUpdatedDto, toBeUpdatedEntity);

        } else {
            throw new NotFoundException("No Changes found");
        }
    }

    @Override
    public DeviceConfigResponseDto deleteById(int id) throws Exception {
        DeviceConfig entity = dao.findById(id);
        DeviceConfigResponseDto responseDto = DeviceConfigMapper.toDto(new DeviceConfigResponseDto(), entity);

        dao.deleteById(id);
        globalAuditDao.create(new GlobalAuditEntry(resource, UserOperationEnum.DELETE.getValue(),
                id, objectMapper.writeValueAsString(responseDto), null,
                userBeanUtil.getRemoteAdr()));

        return responseDto;
    }
}
