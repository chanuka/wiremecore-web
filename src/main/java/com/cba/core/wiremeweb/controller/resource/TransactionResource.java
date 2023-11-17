package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.dto.TransactionCoreResponseDto;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface TransactionResource {

    @GetMapping("/terminals")
    ResponseEntity<List<TerminalResponseDto>> getAllTerminals() throws Exception;

    @GetMapping("/merchants")
    ResponseEntity<List<MerchantResponseDto>> getAllMerchants() throws Exception;

    @GetMapping("/partners")
    ResponseEntity<List<MerchantCustomerResponseDto>> getAllMerchantCustomers() throws Exception;

    @GetMapping()
    ResponseEntity<PaginationResponse<TransactionCoreResponseDto>> getAllTransactions(@RequestParam(defaultValue = "") String dateFrom,
                                                                                      @RequestParam(defaultValue = "") String dateTo,
                                                                                      @RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("summary")
    ResponseEntity<Map<String, ArrayList<Map<String, Object>>>> getAllTransactionSummary(@RequestParam(defaultValue = "") String dateFrom,
                                                                                                       @RequestParam(defaultValue = "") String dateTo,
                                                                                                       @RequestParam(defaultValue = "") String queryBy) throws Exception;
}
