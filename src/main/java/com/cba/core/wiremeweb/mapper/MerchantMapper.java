package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.MerchantRequestDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.MerchantCustomer;
import com.cba.core.wiremeweb.model.Status;

public class MerchantMapper {

    public static MerchantResponseDto toDto(Merchant merchant) {
        MerchantResponseDto merchantResponseDto = new MerchantResponseDto();
        merchantResponseDto.setName(merchant.getName());
        merchantResponseDto.setMerchantId(merchant.getMerchantId());
        merchantResponseDto.setPartnerId(merchant.getMerchantCustomer().getId());
        merchantResponseDto.setEmail(merchant.getEmail());
        merchantResponseDto.setDistrict(merchant.getDistrict());
        merchantResponseDto.setProvince(merchant.getProvince());
        merchantResponseDto.setStatus(merchant.getStatus().getStatusCode());
        merchantResponseDto.setId(merchant.getId());
        return merchantResponseDto;
    }

    public static Merchant toModel(MerchantRequestDto merchantRequestDto) {
        Merchant merchant = new Merchant();
        merchant.setName(merchantRequestDto.getName());
        merchant.setMerchantId(merchantRequestDto.getMerchantId());
        merchant.setProvince(merchantRequestDto.getProvince());
        merchant.setDistrict(merchantRequestDto.getDistrict());
        merchant.setStatus(new Status(merchantRequestDto.getStatus()));
        merchant.setMerchantCustomer(new MerchantCustomer(merchantRequestDto.getPartnerId()));
        merchant.setEmail(merchantRequestDto.getEmail());
        return merchant;
    }
}
