package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.model.DeviceConfig;

public interface DeviceConfigDao {

    DeviceConfigResponseDto findById(int id) throws Exception;

    DeviceConfigResponseDto create(DeviceConfigRequestDto requestDto) throws Exception;

    DeviceConfigResponseDto update(int id, DeviceConfigRequestDto requestDto) throws Exception;

    DeviceConfigResponseDto deleteById(int id) throws Exception;


}
