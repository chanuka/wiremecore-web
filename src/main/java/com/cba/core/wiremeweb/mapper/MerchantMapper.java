package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.model.Mcc;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;

public class MerchantMapper {

    public static MerchantResponseDto toDto(Merchant entity) {
        MerchantResponseDto responseDto = new MerchantResponseDto();
        responseDto.setName(entity.getName());
        responseDto.setMerchantId(entity.getMerchantId());
        responseDto.setPartnerId(entity.getMerchantCustomer().getId());
        responseDto.setPartnerName(entity.getMerchantCustomer().getName());
        responseDto.setEmail(entity.getEmail());
        responseDto.setDistrict(entity.getDistrict());
        responseDto.setProvince(entity.getProvince());
        responseDto.setLat(entity.getLat());
        responseDto.setLon(entity.getLon());
        responseDto.setRadius(entity.getRadius());
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
        entity.setAddress(requestDto.getAddress());
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setMerchantCustomer(new MerchantCustomer(requestDto.getPartnerId()));
        entity.setEmail(requestDto.getEmail());
        entity.setLat(requestDto.getLat());
        entity.setLon(requestDto.getLon());
        entity.setRadius(requestDto.getRadius());
        return entity;
    }
}
