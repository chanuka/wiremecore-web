package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.ResourceDao;
import com.cba.core.wiremeweb.dto.ResourceResponseDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.ResourceMapper;
import com.cba.core.wiremeweb.model.Resource;
import com.cba.core.wiremeweb.repository.ResourceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ResourceDaoImpl implements ResourceDao {

    private final ResourceRepository repository;

    @Override
    public List<Resource> findAll() throws Exception {
        return repository.findAll();
    }
}
