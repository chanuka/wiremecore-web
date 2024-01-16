package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GeneralDao;
import com.cba.core.wiremeweb.model.District;
import com.cba.core.wiremeweb.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GeneralDaoImpl implements GeneralDao {

    private final DistrictRepository repository;

    @Override
    public List<District> findAllDistrict() throws Exception {
        return repository.findAll();
    }
}
