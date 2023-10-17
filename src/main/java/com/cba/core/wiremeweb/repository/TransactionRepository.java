package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.TransactionCore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Date;

public interface TransactionRepository extends JpaRepository<TransactionCore, Integer>, JpaSpecificationExecutor<TransactionCore> {

    Page<TransactionCore> findByDateTimeBetween(Date fromDate, Date toDate, Pageable pageable);
}
