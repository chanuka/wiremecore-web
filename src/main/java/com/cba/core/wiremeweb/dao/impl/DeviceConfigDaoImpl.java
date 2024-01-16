package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.DeviceConfigDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.DeviceConfig;
import com.cba.core.wiremeweb.repository.DeviceConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

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
