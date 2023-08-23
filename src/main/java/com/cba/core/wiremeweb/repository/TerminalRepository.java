package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Terminal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TerminalRepository extends JpaRepository<Terminal, Integer>, JpaSpecificationExecutor<Terminal> {
}
