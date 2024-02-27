package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.TerminalRequestDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.model.*;

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
        responseDto.setCurrency(entity.getCurrency().getCode());
        responseDto.setRemarks(entity.getRemarks());
        responseDto.setDailyExpSale(entity.getDailyExpSale());
        responseDto.setIsMkeEnabled((entity.getIsMkeEnabled() == 1 )?true:false);
        responseDto.setIsOfflineEnabled((entity.getIsOfflineEnabled() == 1 )?true:false);
        responseDto.setIsPreauthEnabled((entity.getIsPreauthEnabled() == 1 )?true:false);
        responseDto.setIsVoidEnabled((entity.getIsVoidEnabled() == 1 )?true:false);
        return responseDto;
    }

    public static Terminal toModel(TerminalRequestDto requestDto,Merchant merchant) {
        Terminal entity = new Terminal();
        entity.setTerminalId(requestDto.getTerminalId());
        entity.setMerchant(merchant);
        entity.setStatus(new Status(requestDto.getStatus()));
        entity.setDevice(new Device(requestDto.getDeviceId()));
        entity.setCurrency(new Currency(requestDto.getCurrency()));
        entity.setRemarks(requestDto.getRemarks());
        entity.setDailyExpSale(requestDto.getDailyExpSale());
        entity.setIsMkeEnabled((byte)(requestDto.getIsMkeEnabled()?1:0));
        entity.setIsOfflineEnabled((byte)(requestDto.getIsOfflineEnabled()?1:0));
        entity.setIsPreauthEnabled((byte)(requestDto.getIsPreauthEnabled()?1:0));
        entity.setIsVoidEnabled((byte)(requestDto.getIsVoidEnabled()?1:0));
        return entity;
    }
}
