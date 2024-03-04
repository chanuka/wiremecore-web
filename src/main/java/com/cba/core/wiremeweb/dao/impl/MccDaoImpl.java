package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.MccDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.Mcc;
import com.cba.core.wiremeweb.repository.MccRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MccDaoImpl implements MccDao {

    private final MccRepository repository;

    @Override
    public Mcc findByCode(String code) throws Exception {
        return repository.findByCode(code).orElseThrow(() -> new NotFoundException("MCC not found"));
    }
}
