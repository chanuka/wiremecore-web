package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.MerchantCustomerRequestDto;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;

public class MerchantCustomerMapper {

    public static MerchantCustomerResponseDto toDto(MerchantCustomer merchantCustomer) {
        MerchantCustomerResponseDto merchantCustomerResponseDto = new MerchantCustomerResponseDto();
        merchantCustomerResponseDto.setName(merchantCustomer.getName());
        merchantCustomerResponseDto.setAddress(merchantCustomer.getAddress());
        merchantCustomerResponseDto.setContactNo(merchantCustomer.getContactNo());
        merchantCustomerResponseDto.setEmail(merchantCustomer.getEmail());
        merchantCustomerResponseDto.setStatus(merchantCustomer.getStatus().getStatusCode());
        merchantCustomerResponseDto.setId(merchantCustomer.getId());
        return merchantCustomerResponseDto;
    }

    public static MerchantCustomer toModel(MerchantCustomerRequestDto merchantCustomerRequestDto) {
        MerchantCustomer merchantCustomer = new MerchantCustomer();
        merchantCustomer.setName(merchantCustomerRequestDto.getName());
        merchantCustomer.setAddress(merchantCustomerRequestDto.getAddress());
        merchantCustomer.setStatus(new Status(merchantCustomerRequestDto.getStatus()));
        merchantCustomer.setContactNo(merchantCustomerRequestDto.getContactNo());
        merchantCustomer.setEmail(merchantCustomerRequestDto.getEmail());
        return merchantCustomer;
    }
}
