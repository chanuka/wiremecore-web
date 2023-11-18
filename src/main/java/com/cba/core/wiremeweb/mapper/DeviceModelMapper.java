package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DeviceModelRequestDto;
import com.cba.core.wiremeweb.dto.DeviceModelResponseDto;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.model.Status;

public class DeviceModelMapper {

    public static DeviceModelResponseDto toDto(DeviceModel deviceModel) {
        DeviceModelResponseDto responseDto = new DeviceModelResponseDto();
        responseDto.setId(deviceModel.getId());
        responseDto.setName(deviceModel.getName());
        responseDto.setImg(deviceModel.getImg());
        responseDto.setStatus(deviceModel.getStatus().getStatusCode());
        responseDto.setDeviceVendor(DeviceVendorMapper.toDto(deviceModel.getDeviceVendor()));
        return responseDto;
    }

    public static DeviceModel toModel(DeviceModelRequestDto deviceModelRequestDto) {
        DeviceModel entity = new DeviceModel();
        entity.setName(deviceModelRequestDto.getName());
        entity.setImg(deviceModelRequestDto.getImg());
        Status status = new Status(deviceModelRequestDto.getStatus());
        DeviceVendor deviceVendor = new DeviceVendor(deviceModelRequestDto.getDeviceVendor());
        entity.setDeviceVendor(deviceVendor);
        entity.setStatus(status);
        return entity;
    }
}
