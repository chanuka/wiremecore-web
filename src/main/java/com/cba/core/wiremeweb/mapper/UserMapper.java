package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.UserRequestDto;
import com.cba.core.wiremeweb.dto.UserResponseDto;
import com.cba.core.wiremeweb.model.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserMapper {

    public static UserResponseDto toDto(User entity) {
        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setUserName(entity.getUserName());
        responseDto.setId(entity.getId());
        responseDto.setContactNo(entity.getContactNo());
        responseDto.setEmail(entity.getEmail());
        responseDto.setMerchantId(entity.getMerchant() != null ? entity.getMerchant().getMerchantId() : null);
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

        Device device = new Device(requestDto.getDeviceId() != null ? requestDto.getDeviceId() : null);
        entity.setDevice(device);

        Merchant merchant = new Merchant(requestDto.getMerchantId() != null ? requestDto.getMerchantId() : null);
        entity.setMerchant(merchant);

        MerchantCustomer merchantCustomer = new MerchantCustomer(requestDto.getPartnerId() != null ? requestDto.getPartnerId() : null);
        entity.setMerchantCustomer(merchantCustomer);

        return entity;
    }

}
