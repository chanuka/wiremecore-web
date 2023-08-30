package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.ResourceDao;
import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.MerchantMapper;
import com.cba.core.wiremeweb.mapper.ResourceMapper;
import com.cba.core.wiremeweb.model.Merchant;
import com.cba.core.wiremeweb.model.Resource;
import com.cba.core.wiremeweb.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Transactional
@RequiredArgsConstructor
public class ResourceDaoImpl implements ResourceDao {

    private final ResourceRepository repository;

    @Override
    public List<ResourceResponseDto> findAll() throws Exception {
        List<Resource> entityList = repository.findAll();
        if (entityList.isEmpty()) {
            throw new NotFoundException("No Resources found");
        }
        return entityList
                .stream()
                .map(ResourceMapper::toDto)
                .collect(Collectors.toList());
    }
}
