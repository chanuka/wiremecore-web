package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.TransactionDao;
import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;

    @Override
    public List<TerminalResponseDto> findAllTerminals() throws Exception {
        return transactionDao.findAllTerminals();
    }

    @Override
    public List<MerchantResponseDto> getAllMerchants() throws Exception {
        return transactionDao.getAllMerchants();
    }

    @Override
    public List<MerchantCustomerResponseDto> getAllMerchantCustomers() throws Exception {
        return transactionDao.getAllMerchantCustomers();
    }

    @Override
    public Page<TransactionCoreResponseDto> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        return transactionDao.getAllTransactions(dateFrom, dateTo, page, pageSize);
    }

    @Override
    public Map<String, ArrayList<Map<String, Object>>> getAllTransactionSummary(String dateFrom, String dateTo, String queryBy) throws Exception {
        return transactionDao.getAllTransactionSummary(dateFrom, dateTo, queryBy);
    }
}
