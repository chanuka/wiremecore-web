package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

public interface MerchantResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/terminals/{id}")
    ResponseEntity<List<TerminalResponseDto>> findTerminalsByMerchant(@PathVariable(value = "id") int id,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/all")
    ResponseEntity<List<MerchantResponseDto>> getAllMerchants() throws Exception;

    @PostMapping("/search/all")
    ResponseEntity<PaginationResponse<T>> searchAllByPageWiseByKey(@RequestBody Map<String, String> searchParameter,
                                                              @RequestParam(defaultValue = "0") int page,
                                                              @RequestParam(defaultValue = "5") int pageSize)  throws Exception;

}
