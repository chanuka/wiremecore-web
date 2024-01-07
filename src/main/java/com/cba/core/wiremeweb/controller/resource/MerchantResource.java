package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.MerchantCustomerResponseDto;
import com.cba.core.wiremeweb.dto.MerchantResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface MerchantResource<T, K> extends GenericResource<T, K> {

    @GetMapping("/terminals/{id}")
    ResponseEntity<List<TerminalResponseDto>> findTerminalsByMerchant(@PathVariable(value = "id") int id,
                                                                      @RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "5") int pageSize) throws Exception;

    @GetMapping("/all")
    ResponseEntity<List<MerchantResponseDto>> getAllMerchants() throws Exception;

}
