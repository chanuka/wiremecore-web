package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GlobalAuditEntryRepository extends JpaRepository<GlobalAuditEntry, Integer> {
}
