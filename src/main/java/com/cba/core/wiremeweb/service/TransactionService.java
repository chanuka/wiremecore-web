package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TransactionService {

    List<TerminalResponseDto> findAllTerminals() throws Exception;

    List<MerchantResponseDto> getAllMerchants() throws Exception;

    List<MerchantCustomerResponseDto> getAllMerchantCustomers() throws Exception;

    Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception;

    Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception;

}
