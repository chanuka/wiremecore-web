package com.cba.core.wiremeweb.repository;

import com.cba.core.wiremeweb.model.Mcc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface MccRepository extends JpaRepository<Mcc, Integer>, JpaSpecificationExecutor<Mcc> {

    Optional<Mcc> findByCode(String code);
}
