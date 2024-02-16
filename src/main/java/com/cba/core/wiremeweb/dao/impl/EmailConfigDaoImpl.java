package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.EmailConfigDao;
import com.cba.core.wiremeweb.exception.NotFoundException;
import com.cba.core.wiremeweb.model.EmailConfig;
import com.cba.core.wiremeweb.repository.EmailConfigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmailConfigDaoImpl implements EmailConfigDao {

    private final EmailConfigRepository repository;

    @Override
    public EmailConfig findByAction(String action) throws Exception {
        return repository.findByAction(action)
                .orElseThrow(() -> new NotFoundException("Email config Not Found for given action"));
    }
}
