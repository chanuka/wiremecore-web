package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;

public class MerchantCustomerMapper {

    public static MerchantCustomerResponseDto toDto(MerchantCustomer entity) {
        MerchantCustomerResponseDto responseDto = new MerchantCustomerResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setAddress(entity.getAddress());
        responseDto.setContactNo(entity.getContactNo());
        responseDto.setEmail(entity.getEmail());
        responseDto.setStatus(entity.getStatus().getStatusCode());
        responseDto.setId(entity.getId());
        return responseDto;
    }

    public static MerchantCustomer toModel(MerchantCustomerRequestDto requestDto) {
        MerchantCustomer entity = new MerchantCustomer();
        entity.setName(requestDto.getName());
        entity.setAddress(requestDto.getAddress());
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setContactNo(requestDto.getContactNo());
        entity.setEmail(requestDto.getEmail());
        return entity;
    }
}
