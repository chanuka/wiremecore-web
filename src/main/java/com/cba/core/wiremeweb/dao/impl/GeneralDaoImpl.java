package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GeneralDao;
import com.cba.core.wiremeweb.dto.DistrictDto;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.mapper.DistrictMapper;
import com.cba.core.wiremeweb.model.District;
import com.cba.core.wiremeweb.repository.DistrictRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GeneralDaoImpl implements GeneralDao {

    private final DistrictRepository repository;

    @Override
    public List<District> findAllDistrict() throws Exception {
        return repository.findAll();
    }
}
