package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.model.Device;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Status;
import com.cba.core.wiremeweb.model.Terminal;

public class TerminalMapper {

    public static TerminalResponseDto toDto(Terminal entity) {
        TerminalResponseDto responseDto = new TerminalResponseDto();
        responseDto.setTerminalId(entity.getTerminalId());
        responseDto.setDeviceId(entity.getDevice().getId());
        responseDto.setMerchantId(entity.getMerchant().getMerchantId());
        responseDto.setStatus(entity.getStatus().getStatusCode());
        responseDto.setPartnerName(entity.getMerchant().getMerchantCustomer().getName());
        responseDto.setDeviceType(entity.getDevice().getDeviceType());
        responseDto.setSerialNo(entity.getDevice().getSerialNo());
        responseDto.setMerchantName(entity.getMerchant().getName());
        responseDto.setId(entity.getId());
        return responseDto;
    }

    public static Terminal toModel(TerminalRequestDto requestDto,Merchant merchant) {
        Terminal entity = new Terminal();
        entity.setTerminalId(requestDto.getTerminalId());
        entity.setMerchant(merchant);
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setDevice(new Device(requestDto.getDeviceId()));
        return entity;
    }
}
