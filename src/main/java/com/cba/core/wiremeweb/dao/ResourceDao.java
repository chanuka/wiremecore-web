package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.model.Resource;

import java.util.List;

public interface ResourceDao {
    List<Resource> findAll() throws Exception;
}
