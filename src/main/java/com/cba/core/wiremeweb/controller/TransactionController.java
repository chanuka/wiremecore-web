package com.cba.core.wiremeweb.controller;

import com.cba.core.wiremeweb.controller.resource.TransactionResource;
import com.cba.core.wiremeweb.dto.*;
import com.cba.core.wiremeweb.service.GenericService;
import com.cba.core.wiremeweb.service.TransactionService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

@Component
@RequiredArgsConstructor
@RequestMapping("/${application.resource.transactions}")
@Tag(name = "Transaction Management", description = "Provides Transaction Management API's")
public class TransactionController implements TransactionResource {

    private static final Logger logger = LoggerFactory.getLogger(TerminalController.class);

    private final TransactionService transactionService;
    private final MessageSource messageSource;

    @Override
    public ResponseEntity<List<TerminalResponseDto>> getAllTerminals() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TERMINAL_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<TerminalResponseDto> responseDtolist = transactionService.findAllTerminals();
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantResponseDto>> getAllMerchants() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<MerchantResponseDto> responseDtoList = transactionService.getAllMerchants();
            return ResponseEntity.ok().body(responseDtoList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<MerchantCustomerResponseDto>> getAllMerchantCustomers() throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("MERCHANT_CUSTOMER_GET_ALL_DEBUG", null, currentLocale));
        try {
            List<MerchantCustomerResponseDto> responseDtolist = transactionService.getAllMerchantCustomers();
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<List<TransactionCoreResponseDto>> getAllTransactions(String dateFrom, String dateTo, int page, int pageSize) throws Exception {
        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_ALL_DEBUG", null, currentLocale));
        try {
            Page<TransactionCoreResponseDto> responseDtolist = transactionService.getAllTransactions(dateFrom, dateTo, page, pageSize);
            return ResponseEntity.ok().body(responseDtolist.getContent());
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllTransactionSummary(String dateFrom, String dateTo,
                                                                                                String queryBy) throws Exception {

        Locale currentLocale = LocaleContextHolder.getLocale();
        logger.debug(messageSource.getMessage("TRANSACTION_GET_SUMMARY_DEBUG", null, currentLocale));
        try {
            Map<String, ArrayList<Map<String, Object>>> responseDtolist = transactionService.getAllTransactionSummary(dateFrom, dateTo, queryBy);
            return ResponseEntity.ok().body(responseDtolist);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

}
