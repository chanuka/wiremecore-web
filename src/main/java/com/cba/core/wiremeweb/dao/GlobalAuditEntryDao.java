package com.cba.core.wiremeweb.dao;

public interface GlobalAuditEntryDao {

    public void newRevision(Object revisionEntity, String resource, Integer effectedId, String operation,
                            String oldValue, String newValue, String ipAddress) throws Exception;

}
