package com.cba.core.wiremeweb.service.impl;

import com.cba.core.wiremeweb.dao.ResourceDao;
import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.ResourceMapper;
import com.cba.core.wiremeweb.model.Resource;
import com.cba.core.wiremeweb.service.ResourceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {

    private final ResourceDao dao;

    @Override
    public List<ResourceResponseDto> findAll() throws Exception {
        List<Resource> entityList = dao.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Resources found");
        }
        return entityList
                .stream()
                .map(ResourceMapper::toDto)
                .collect(Collectors.toList());
    }
}
