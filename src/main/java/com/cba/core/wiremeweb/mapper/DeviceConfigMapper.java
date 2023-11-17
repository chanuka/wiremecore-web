package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DeviceConfigRequestDto;
import com.cba.core.wiremeweb.dto.DeviceConfigResponseDto;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceConfig;
import com.cba.core.wiremeweb.model.Status;

public class DeviceConfigMapper {

    public static DeviceConfigResponseDto toDto(DeviceConfigResponseDto responseDto, DeviceConfig deviceConfig) {
//        DeviceConfigResponseDto responseDto = new DeviceConfigResponseDto();
        responseDto.setId(deviceConfig.getId());
        responseDto.setConfigType(deviceConfig.getConfigType());
        responseDto.setDeviceId(deviceConfig.getDevice().getId());
//        responseDto.setDeviceId(deviceConfig.getDevice().getId());
        responseDto.setStatus(deviceConfig.getStatus().getStatusCode());
//        responseDto.setMerchants(deviceConfig.getId());
//        responseDto.setHosts(deviceConfig.getSerialNo());
//        responseDto.setMerchants(deviceConfig.getStatus().getStatusCode());
        return responseDto;
    }

    public static DeviceConfig toModel(DeviceConfigRequestDto requestDto) {
        DeviceConfig entity = new DeviceConfig();
        entity.setConfigType(requestDto.getConfigType());
        Device device = new Device(requestDto.getDeviceId());
        entity.setDevice(device);
        Status status = new Status(requestDto.getStatus());
        entity.setStatus(status);
        return entity;
    }
}
