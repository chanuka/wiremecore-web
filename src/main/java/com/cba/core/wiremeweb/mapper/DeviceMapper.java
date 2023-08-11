package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DeviceRequestDto;
import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.Status;

import java.sql.SQLException;

public class DeviceMapper {

    public static DeviceResponseDto toDto(Device device) {
        DeviceResponseDto deviceResponseDto = new DeviceResponseDto();
        deviceResponseDto.setDeviceType(device.getDeviceType());
        deviceResponseDto.setEmiNo(device.getEmiNo());
        deviceResponseDto.setId(device.getId());
        deviceResponseDto.setSerialNo(device.getSerialNo());
        deviceResponseDto.setActive(device.getStatus().getStatusCode().equals("ACTV"));
        return deviceResponseDto;
    }

    public static Device toModel(DeviceRequestDto deviceRequestDto) {
        Device device = new Device();
        device.setDeviceType(deviceRequestDto.getDeviceType());
        device.setEmiNo(deviceRequestDto.getEmiNo());
        device.setSerialNo(deviceRequestDto.getSerialNo());
        Status status = new Status((deviceRequestDto.isActive()) ? "ACTV" : "DACT");
        device.setStatus(status);
        return device;
    }
    }
