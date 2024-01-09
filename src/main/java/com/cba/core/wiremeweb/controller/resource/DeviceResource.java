package com.cba.core.wiremeweb.controller.resource;

import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.dto.DeviceVendorResponseDto;
import com.cba.core.wiremeweb.dto.DistributionResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.model.DeviceVendor;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

public interface DeviceResource<T, K> extends GenericResource<T, K> {

    @PostMapping("/distribution")
    ResponseEntity<List<DistributionResponseDto>> getDeviceDistribution(@RequestBody Map<String, String> grouping) throws Exception;

    @PostMapping("/geo-fence")
    ResponseEntity<PaginationResponse<DeviceResponseDto>> getGeoFenceDevice(@RequestBody Map<String, Object> filter) throws Exception;

}
