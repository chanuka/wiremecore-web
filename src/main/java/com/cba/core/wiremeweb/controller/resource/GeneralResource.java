package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@Validated
public interface GeneralResource {

    @GetMapping("/general/districts")
    ResponseEntity<Map<String, List<DistrictDto>>> getDistricts() throws Exception;
}
