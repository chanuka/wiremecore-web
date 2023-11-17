package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;

public interface DeviceConfigService {

    DeviceConfigResponseDto findById(int id) throws Exception;

    DeviceConfigResponseDto create(DeviceConfigRequestDto requestDto) throws Exception;

    DeviceConfigResponseDto update(int id, DeviceConfigRequestDto requestDto) throws Exception;

    DeviceConfigResponseDto deleteById(int id) throws Exception;


}
