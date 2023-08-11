package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.GlobalAuditEntry;

import java.util.List;

public interface GlobalAuditEntryDao {

    public GlobalAuditEntry createRevision(String resource, Integer effectedId, String operation,
                                           String oldValue, String newValue, String ipAddress) throws Exception;


    public void saveRevision(GlobalAuditEntry globalAuditEntry) throws Exception;

    public void saveAllRevision(List<GlobalAuditEntry> globalAuditEntryList) throws Exception;
}
