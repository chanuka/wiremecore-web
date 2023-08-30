package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.ResourceDao;
import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.service.ResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceDao dao;

    @Override
    public List<ResourceResponseDto> findAll() throws Exception {
        return dao.findAll();
    }
}
