package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.DeviceVendorRequestDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
import com.cba.core.wiremeweb.model.DeviceModel;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.model.Status;

import java.util.ArrayList;

public class DeviceVendorMapper {

    public static DeviceVendorResponseDto toDto(DeviceVendor deviceVendor) {
        DeviceVendorResponseDto responseDto = new DeviceVendorResponseDto();
        responseDto.setId(deviceVendor.getId());
        responseDto.setName(deviceVendor.getName());
        responseDto.setImg(deviceVendor.getImg());
        responseDto.setStatus(deviceVendor.getStatus().getStatusCode());
        return responseDto;
    }

    public static DeviceVendor toModel(DeviceVendorRequestDto deviceVendorRequestDto) {
        DeviceVendor entity = new DeviceVendor();
        entity.setName(deviceVendorRequestDto.getName());
        entity.setImg(deviceVendorRequestDto.getImg());
        Status status = new Status(deviceVendorRequestDto.getStatus());
        entity.setStatus(status);
        return entity;
    }
}
