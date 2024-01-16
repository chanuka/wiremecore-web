package com.cba.core.wiremeweb.dao;

import com.cba.core.wiremeweb.model.GlobalAuditEntry;

public interface GlobalAuditDao {

    void create(GlobalAuditEntry auditEntry) throws Exception;

}
