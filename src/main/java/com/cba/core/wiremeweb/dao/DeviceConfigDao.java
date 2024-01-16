package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.DeviceConfig;

public interface DeviceConfigDao {

    DeviceConfig findById(int id) throws Exception;

    DeviceConfig create(DeviceConfig toInsert) throws Exception;

    DeviceConfig update(int id, DeviceConfig requestDto) throws Exception;

    void deleteById(int id) throws Exception;


}
