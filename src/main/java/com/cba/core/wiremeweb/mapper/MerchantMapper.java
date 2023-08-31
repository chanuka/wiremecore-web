package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;

public class MerchantMapper {

    public static MerchantResponseDto toDto(Merchant entity) {
        MerchantResponseDto responseDto = new MerchantResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setMerchantId(entity.getMerchantId());
        responseDto.setPartnerId(entity.getMerchantCustomer().getId());
        responseDto.setEmail(entity.getEmail());
        responseDto.setDistrict(entity.getDistrict());
        responseDto.setProvince(entity.getProvince());
        responseDto.setStatus(entity.getStatus().getStatusCode());
        responseDto.setId(entity.getId());
        return responseDto;
    }

    public static Merchant toModel(MerchantRequestDto requestDto) {
        Merchant entity = new Merchant();
        entity.setName(requestDto.getName());
        entity.setMerchantId(requestDto.getMerchantId());
        entity.setProvince(requestDto.getProvince());
        entity.setDistrict(requestDto.getDistrict());
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
        entity.setEmail(requestDto.getEmail());
        return entity;
    }
}
