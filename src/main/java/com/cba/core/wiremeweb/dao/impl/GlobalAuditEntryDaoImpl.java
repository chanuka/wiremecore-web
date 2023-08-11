package com.cba.core.wiremeweb.dao.impl;

import com.cba.core.wiremeweb.dao.GlobalAuditEntryDao;
import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import com.cba.core.wiremeweb.repository.GlobalAuditEntryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class GlobalAuditEntryDaoImpl implements GlobalAuditEntryDao {

    private final GlobalAuditEntryRepository globalAuditEntryRepository;

    public GlobalAuditEntry createRevision(String resource, Integer effectedId, String operation,
                                           String oldValue, String newValue, String ipAddress) throws Exception {

        GlobalAuditEntry globalAuditEntry = new GlobalAuditEntry();

        // Set entity type, entity ID, revision type, modified fields, old value, new value, etc.
        globalAuditEntry.setResource(resource);
        globalAuditEntry.setEffectedId(effectedId); // Set the appropriate revision type
        globalAuditEntry.setOperation(operation);
        globalAuditEntry.setOldValue(oldValue);
        globalAuditEntry.setNewValue(newValue);
        globalAuditEntry.setIpAddress(ipAddress);

        return globalAuditEntry;
    }


    @Override
    public void saveRevision(GlobalAuditEntry globalAuditEntry) throws Exception {
        globalAuditEntryRepository.save(globalAuditEntry);
    }

    @Override
    public void saveAllRevision(List<GlobalAuditEntry> globalAuditEntryList) throws Exception {
        globalAuditEntryRepository.saveAll(globalAuditEntryList);
    }
}
