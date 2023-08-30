package com.cba.core.wiremeweb.service;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;

import java.util.List;

public interface ResourceService {
    List<ResourceResponseDto> findAll() throws Exception;
}
