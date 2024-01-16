package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditDao;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class GlobalAuditDaoImpl implements GlobalAuditDao {

    private final GlobalAuditEntryRepository repository;

    @Override
    public void create(GlobalAuditEntry auditEntry) throws Exception {
        repository.save(auditEntry);
    }
}
