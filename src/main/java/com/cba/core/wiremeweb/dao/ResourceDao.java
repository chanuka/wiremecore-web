package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;

import java.util.List;

public interface ResourceDao {
    List<ResourceResponseDto> findAll() throws Exception;
}
