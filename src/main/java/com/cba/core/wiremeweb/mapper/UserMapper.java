package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.model.*;

public class UserMapper {

    public static UserResponseDto toDto(User entity) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setUserName(entity.getUserName());
        responseDto.setId(entity.getId());
        responseDto.setContactNo(entity.getContactNo());
        responseDto.setEmail(entity.getEmail());
        responseDto.setStatus(entity.getStatus().getStatusCode());
        responseDto.setDeviceId(entity.getDevice() != null ? entity.getDevice().getId() : null);
        responseDto.setMerchantId(entity.getMerchant() != null ? entity.getMerchant().getId() : null);
        responseDto.setMerchantName(entity.getMerchant() != null ? entity.getMerchant().getName() : null);
        responseDto.setPartnerId(entity.getMerchantCustomer() != null ? entity.getMerchantCustomer().getId() : null);
        responseDto.setPartnerName(entity.getMerchantCustomer() != null ? entity.getMerchantCustomer().getName() : null);
        return responseDto;
    }

    public static User toModel(UserRequestDto requestDto) {
        User entity = new User();
        entity.setName(requestDto.getName());
        entity.setUserName(requestDto.getUserName());
        entity.setContactNo(requestDto.getContactNo());
        entity.setEmail(requestDto.getEmail());
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setUserType(new UserType(requestDto.getUserType()));

        if (requestDto.getDeviceId() != null) {
            entity.setDevice(new Device(requestDto.getDeviceId()));
        } else {
            entity.setDevice(null);
        }

        if (requestDto.getMerchantId() != null) {
            entity.setMerchant(new Merchant(requestDto.getMerchantId()));
        } else {
            entity.setMerchant(null);
        }

        if (requestDto.getPartnerId() != null) {
            entity.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
        } else {
            entity.setMerchantCustomer(null);
        }

        return entity;
    }

}
