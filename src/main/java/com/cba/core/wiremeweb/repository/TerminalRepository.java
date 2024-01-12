package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Terminal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

public interface TerminalRepository extends JpaRepository<Terminal, Integer>, JpaSpecificationExecutor<Terminal> {
    Page<Terminal> findAllByMerchant_Id(int id, Pageable pageable);
}
