package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.DistributionResponseDto;
import com.cba.core.wiremeweb.dto.TerminalResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface DeviceService<T, K> extends GenericService<T, K> {

    List<DistributionResponseDto> getDeviceDistribution(Map<String, String> grouping) throws Exception;
}
