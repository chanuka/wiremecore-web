package com.cba.core.wiremeweb.mapper;

import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.model.TransactionCore;

public class TransactionCoreMapper {

    public static TransactionCoreResponseDto toDto(TransactionCore entity) {
        TransactionCoreResponseDto responseDto = new TransactionCoreResponseDto();
        responseDto.setAmount(entity.getAmount());
        responseDto.setAuthCode(entity.getAuthCode());
        responseDto.setBatchNo(entity.getBatchNo());
        responseDto.setCardLabel(entity.getCardLabel());
        responseDto.setCurrency(entity.getCurrency());
        responseDto.setCustMobile(entity.getCustMobile());
        responseDto.setDateTime(entity.getDateTime());
        responseDto.setDccCurrency(entity.getDccCurrency());
        responseDto.setDccTranAmount(entity.getDccTranAmount());
        responseDto.setIssettled(entity.getIssettled());
        responseDto.setId(entity.getId());
        responseDto.setEntryMode(entity.getEntryMode());
        responseDto.setInvoiceNo(entity.getInvoiceNo());
        responseDto.setMerchantId(entity.getMerchantId());
        responseDto.setTerminalId(entity.getTerminalId());
        responseDto.setSettledMethod(entity.getSettledMethod());
        responseDto.setNii(entity.getNii());
        responseDto.setRrn(entity.getRrn());
        responseDto.setTranType(entity.getTranType());
        responseDto.setPan(entity.getPan());
        responseDto.setSignData(entity.getSignData());
        responseDto.setPaymentMode(entity.getPaymentMode());
        responseDto.setTraceNo(entity.getId());
        return responseDto;
    }
}
