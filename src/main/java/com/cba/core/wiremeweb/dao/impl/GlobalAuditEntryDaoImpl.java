package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditEntryDao;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class GlobalAuditEntryDaoImpl implements GlobalAuditEntryDao {

    private final GlobalAuditEntryRepository globalAuditEntryRepository;

    public void newRevision(Object revisionEntity, String resource, Integer effectedId, String operation,
                            String oldValue, String newValue, String ipAddress) throws Exception {

        GlobalAuditEntry globalAuditEntry = (GlobalAuditEntry) revisionEntity;

        // Set entity type, entity ID, revision type, modified fields, old value, new value, etc.
        globalAuditEntry.setResource(resource);
        globalAuditEntry.setEffectedId(effectedId); // Set the appropriate revision type
        globalAuditEntry.setOperation(operation);
        globalAuditEntry.setOldValue(oldValue);
        globalAuditEntry.setNewValue(newValue);
        globalAuditEntry.setIpAddress(ipAddress);

        globalAuditEntryRepository.save(globalAuditEntry);
    }
}
