package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.Status;

public class DeviceMapper {

    public static DeviceResponseDto toDto(Device device) {
        DeviceResponseDto responseDto = new DeviceResponseDto();
        responseDto.setDeviceType(device.getDeviceType());
        responseDto.setEmiNo(device.getEmiNo());
        responseDto.setId(device.getId());
        responseDto.setSerialNo(device.getSerialNo());
        responseDto.setStatus(device.getStatus().getStatusCode());
        responseDto.setModelId(device.getDeviceModel() != null ? device.getDeviceModel().getId() : null);
        return responseDto;
    }

    public static Device toModel(DeviceRequestDto deviceRequestDto) {
        Device entity = new Device();
        entity.setDeviceType(deviceRequestDto.getDeviceType());
        entity.setEmiNo(deviceRequestDto.getEmiNo());
        entity.setSerialNo(deviceRequestDto.getSerialNo());
        entity.setStatus(new Status(deviceRequestDto.getStatus()));
        entity.setDeviceModel(new DeviceModel(deviceRequestDto.getModelId()));
        return entity;
    }
}
