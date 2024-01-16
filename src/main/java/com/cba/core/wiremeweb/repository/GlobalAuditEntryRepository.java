package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.GlobalAuditEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface GlobalAuditEntryRepository extends JpaRepository<GlobalAuditEntry, Integer> {
}
