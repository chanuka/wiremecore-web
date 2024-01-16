package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceConfigDao;
import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DeviceConfigMapper;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceConfig;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.repository.DeviceConfigRepository;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import com.cba.core.wiremeweb.util.UserBeanUtil;
import com.cba.core.wiremeweb.util.UserOperationEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class DeviceConfigDaoImpl implements DeviceConfigDao {

    private final DeviceConfigRepository repository;

    @Override
    public DeviceConfig findById(int id) throws Exception {
        return repository.findByDevice_Id(id).orElseThrow(() -> new NotFoundException("Device Config not found"));
    }

    @Override
//    @CacheEvict(value = "devices", allEntries = true)
    public DeviceConfig create(DeviceConfig toInsert) throws Exception {
        return repository.save(toInsert);
    }

    @Override
    public DeviceConfig update(int id, DeviceConfig toBeUpdatedEntity) throws Exception {
        return repository.saveAndFlush(toBeUpdatedEntity);
    }

    @Override
    public void deleteById(int id) throws Exception {
        repository.deleteByDevice_Id(id);
    }
}
