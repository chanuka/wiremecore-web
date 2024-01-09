package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.DeviceResponseDto;
import com.cba.core.wiremeweb.dto.DistributionResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import com.cba.core.wiremeweb.util.PaginationResponse;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DeviceDao<T, K> extends GenericDao<T, K> {

    List<DistributionResponseDto> getDeviceDistribution(Map<String, String> grouping) throws Exception;

    PaginationResponse<DeviceResponseDto> getGeoFenceDevice(Map<String, Object> filter) throws Exception;
}
